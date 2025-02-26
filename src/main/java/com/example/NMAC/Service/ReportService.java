package com.example.NMAC.Service;

import com.example.NMAC.Models.AnalysisResult;
import com.example.NMAC.Repository.AnalysisResultRepository;
import com.itextpdf.kernel.pdf.*;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.*;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import org.jfree.chart.*;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;
import java.io.*;
import java.util.List;
import javax.imageio.ImageIO;

@Service
public class ReportService {

    private final AnalysisResultRepository analysisResultRepository;

    public ReportService(AnalysisResultRepository analysisResultRepository) {
        this.analysisResultRepository = analysisResultRepository;
    }

    public void generatePdfReport(String filePath) throws Exception {
        PdfWriter writer = new PdfWriter(filePath);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        // Report Title
        document.add(new Paragraph("📊 Network Analysis Report")
                .setBold().setFontSize(16));
        document.add(new Paragraph("Generated on: " + java.time.LocalDateTime.now()));

        // Create Table with Fixed Column Widths
        Table table = new Table(new float[]{50f, 100f, 100f, 50f}); // Set column widths
        table.setWidth(500); // Fixed width
        table.addHeaderCell("ID");
        table.addHeaderCell("Device Name");
        table.addHeaderCell("Metric");
        table.addHeaderCell("Average Value");

        // Fetch Data from Database
        List<AnalysisResult> reports = analysisResultRepository.findAll();

        for (AnalysisResult report : reports) {
            table.addCell(String.valueOf(report.getId()));
            table.addCell(report.getDevice().getDeviceName());
            table.addCell(report.getMetricType());
            table.addCell(String.valueOf(report.getAverageValue()));
        }

        document.add(table);

        // Add a Chart for Bandwidth Trends
        document.add(new Paragraph("\n📈 Bandwidth Trends Over Time"));
        com.itextpdf.layout.element.Image chartImage = createChartImage(reports);
        document.add(chartImage);

        document.close();
        System.out.println("✅ PDF report generated: " + filePath);
    }

    private com.itextpdf.layout.element.Image createChartImage(List<AnalysisResult> reports) throws Exception {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        for (AnalysisResult report : reports) {
            if ("Bandwidth".equals(report.getMetricType())) {
                dataset.addValue(report.getAverageValue(), "Bandwidth",
                        String.valueOf(report.getTimestamp())); // Ensuring safe conversion
            }
        }

        JFreeChart chart = ChartFactory.createLineChart(
                "Bandwidth Trends", "Time", "Mbps", dataset,
                PlotOrientation.VERTICAL, false, true, false);

        BufferedImage chartImage = chart.createBufferedImage(500, 300);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(chartImage, "png", baos);

        // Convert byte array to iText Image
        ImageData imageData = ImageDataFactory.create(baos.toByteArray());
        return new com.itextpdf.layout.element.Image(imageData);
    }
}
