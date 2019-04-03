package ec.edu.upse.gcf.util;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.util.Map;
import java.util.UUID;

import org.zkoss.zk.ui.Executions;
import org.zkoss.zul.Filedownload;

import ec.edu.upse.gcf.dao.ClaseDAO;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.view.JasperViewer;

public class PrintReport {
	public static final String FORMATO_PDF = "PDF";
	public static final String FORMATO_XLS = "XLS";

	private JasperPrint reportFilled;
	private JasperViewer viewer;
	public void crearReporte(String path, ClaseDAO claseDAO,Map<String, Object> param) {
		try {
			String pathAbsoluto = Executions.getCurrent()
					.getDesktop().getWebApp()
					.getRealPath("/");
			String nombreReporte = pathAbsoluto + path;
			Connection cn = claseDAO.abreConexion();
			//report = (JasperReport) JRLoader.loadObjectFromFile(pathAbsoluto);
			//reportFilled = JasperFillManager.fillReport(report, param, cn);
			String nombreArchivo = null;
			nombreArchivo = pathAbsoluto + "/temp";
			// Obtiene un nombre aleatorio para el reporte
			File folder = new File(nombreArchivo);
			if (folder.exists()) {
			}else {
				folder.mkdir();
			}
			nombreArchivo = "../temp" + "/" + UUID.randomUUID().toString() + ".pdf";
			byte[] b = null;
			b = JasperRunManager.runReportToPdf(nombreReporte, param, cn);
			FileOutputStream fos = new FileOutputStream(nombreArchivo);
			fos.write(b);
			fos.close();
			Filedownload.save(new File(nombreArchivo), "pdf");			
		}catch(Exception ex) {
			ex.printStackTrace();
		}
	}

	public void showReport(String titulo) {
		try {
			viewer = new JasperViewer(reportFilled,false);
			viewer.setTitle(titulo);
			viewer.setVisible(true);
		}catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	public void exportToPDF(String destino) {
		try {
			//JasperExportManager.exportReportToPdfFile(destino);
		}catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	public void imprimirReporte() {
		try {
			JasperPrintManager.printReport(reportFilled, false);
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
}