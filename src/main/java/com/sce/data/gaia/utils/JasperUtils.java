package com.sce.data.gaia.utils;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleWriterExporterOutput;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * jasper function
 *
 * @author bk201
 */
public class JasperUtils {

    /**
     * pdf export, need jasperStream(InputStream jasperStream = this.getClass().getResourceAsStream("/jasperreports/demo.jasper");)
     *
     * @param jasperStream
     * @param params
     * @param dataSource   JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(
     *                     Arrays.asList(
     *                     new DemoDTO(1, "中国人", 300000L),
     *                     new DemoDTO(2, "Bugati", 300000L),
     *                     new DemoDTO(3, "Fort", 300000L),
     *                     new DemoDTO(4, "Audi", 300000L)
     *                     )
     * @return byte[]
     * @throws JRException
     */
    public static byte[] exportPdfByJasper(InputStream jasperStream, Map<String, Object> params,
                                           JRDataSource dataSource) throws JRException {
        JasperReport jr = (JasperReport) JRLoader.loadObject(jasperStream);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jr, params, dataSource);
        return JasperExportManager.exportReportToPdf(jasperPrint);
    }


    public static HttpEntity<byte[]> makePdfHttpEntity(InputStream jasperStream, Map<String, Object> params,
                                                       JRDataSource dataSource, String exportFileName) throws JRException {
        final byte[] data = exportPdfByJasper(jasperStream, params, dataSource);
        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_PDF);
        header.set(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=" + exportFileName + ".pdf");
        header.setContentLength(data.length);
        return new HttpEntity<>(data, header);
    }

    public static byte[] exportXlsxByJasper(InputStream jasperStream, Map<String, Object> params,
                                            JRDataSource dataSource) throws JRException, IOException {
        JasperReport jr = (JasperReport) JRLoader.loadObject(jasperStream);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jr, params, dataSource);
        JRXlsxExporter xlsxExporter = new JRXlsxExporter();
        final byte[] data;
        SimpleXlsxReportConfiguration conf = new SimpleXlsxReportConfiguration();
        conf.setWhitePageBackground(false);
        conf.setDetectCellType(true);
        try (ByteArrayOutputStream xlsReport = new ByteArrayOutputStream()) {
            xlsxExporter.setConfiguration(conf);
            xlsxExporter.setExporterInput(new SimpleExporterInput(jasperPrint));
            xlsxExporter.setExporterOutput(new SimpleOutputStreamExporterOutput(xlsReport));
            xlsxExporter.exportReport();
            data = xlsReport.toByteArray();
        }
        return data;
    }

    public static HttpEntity<byte[]> makeXlsxHttpEntity(InputStream jasperStream, Map<String, Object> params,
                                                        JRDataSource dataSource, String exportFileName) throws JRException, IOException {
        final byte[] data = exportXlsxByJasper(jasperStream, params, dataSource);
        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        header.set(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=" + exportFileName + ".xlsx");
        header.setContentLength(data.length);
        return new HttpEntity<>(data, header);
    }

    /**
     * export csv files
     *
     * @param jasperStream
     * @param params
     * @param dataSource
     * @return
     * @throws JRException
     * @throws IOException
     */
    public static byte[] exportCsvByJasper(InputStream jasperStream, Map<String, Object> params,
                                           JRDataSource dataSource) throws JRException, IOException {
        JasperReport jr = (JasperReport) JRLoader.loadObject(jasperStream);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jr, params, dataSource);
        JRCsvExporter csvExporter = new JRCsvExporter();
        final byte[] data;
        try (ByteArrayOutputStream csvReport = new ByteArrayOutputStream()) {
            csvExporter.setExporterInput(new SimpleExporterInput(jasperPrint));
            csvExporter.setExporterOutput(new SimpleWriterExporterOutput(csvReport));
            csvExporter.exportReport();
            data = csvReport.toByteArray();
        }
        return data;
    }

    public static HttpEntity<byte[]> makeCsvHttpEntity(InputStream jasperStream, Map<String, Object> params,
                                                       JRDataSource dataSource, String exportFileName) throws JRException, IOException {
        final byte[] data = exportXlsxByJasper(jasperStream, params, dataSource);
        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.TEXT_PLAIN);
        header.set(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=" + exportFileName + ".csv");
        header.setContentLength(data.length);
        return new HttpEntity<>(data, header);
    }
}
