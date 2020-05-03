package studyPriceDemoUI;

//Moved from perforce:1666..
/**
* @author Zhoul
* Initial date:  -2020-05-02
* Modified by ...
* In case of broken, update may needed:
*  1. Selenium Webdriver
*  2. Chromedriver
*  3. geckodriver (FF)
*  4. poi-3.9-20121203.jar
*  5. sqljdbc4.jar
*  6. javamail.jar
*  7.json-20160212.jar
*  8. bson-3.30.jar
*  
*   
*   */

import java.awt.AWTException;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
//Test updated 02
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;


public class StudyPriceDemoUIController extends Comlibs {
	private final WebDriver driver;
	final int wt_Secs = 0;
	static String[] vinpxConent = new String[200];
	static String[] stockpxConent = new String[200];
	static String[] lotpxConent = new String[200];
	final static int SINGLE_VIN_RENDER_MAX_WT = 10;
	final static int ALL_VINS_RENDER_MAX_WT = 20;

	public StudyPriceDemoUIController(WebDriver driver, String myId) throws IOException {
		this.driver = driver;
		// String wh1=driver.getWindowHandle();

		String sPageTitle = "VINpx Login";
		boolean existTitle = TitleDisplay(driver, sPageTitle);
		if (existTitle)
			try {
				{
					rwExcel("", true, "Page Title is displayed!...", sPageTitle);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		else {
			rwExcel("", false, "Page title is NOT showing properly. Exceed time limit!",
					"The page title is NOT - " + sPageTitle);
		}
		if (!sPageTitle.equals(driver.getTitle())) {
			// throw new IllegalStateException("The page title is NOT - "
			// + sPageTitle);
			System.out.println("The page title is NOT - " + sPageTitle);
		}
	}

	/*
	 * ------------------------------ Home Page Object Repositories: ------------------------------
	 */
	By dealershipNameField = By.id("dealerName");
	By webSite = By.id("dealerSite");

	static String urlLink;

	public static StudyPRICEDemo loadURL(WebDriver driver, String bURL) throws IOException {
		driver.get(bURL);
		return new StudyPRICEDemo(driver);
	}





	public static void StudyPriceDemoUILogin(WebDriver driver, String brw, String envment, String ver) throws Exception {
		// Load environment parameters
		Properties prop = new Properties();
		// testprop.load(new FileInputStream("data/studyPriceDemoUI.properties"));
		try {
			prop.load(StudyPriceDemoUIController.class.getClassLoader().getResourceAsStream("./data/studyPriceDemoUI.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		String env = prop.getProperty("StudyPrice.environment");
		String versionNum = prop.getProperty("StudyPrice.versionNum");
		String envDevice = prop.getProperty("StudyPrice.envDevice");
		int wt = Integer.parseInt(prop.getProperty("StudyPrice.waitTime"));
		// Initial
		String TCnum;
		Comlibs log =new Comlibs();
		log.rwExcel("", "*********Study Price Demo UI Login TCs**********", "");
		StudyPRICEDemo loginP = new StudyPRICEDemo(driver);
		TCnum = "TC:xxxxxx";
	
	}


	private static String[] fetchOneDemArrayFromPropFile(String propertyName, Properties propFile) {

		// get array split up by the colin
		String a[] = propFile.getProperty(propertyName).split(",");

		return a;
	}

	public static String getVehGUIDfromDealerCodeAndVIN(String dlrCode, String sVin)
			throws ClassNotFoundException, SQLException {
		Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
		Connection conn = DriverManager.getConnection(
				"jdbc:sqlserver://LNOC-Q13V-MSQ2.autodata.org;user=VDVIWebServicesUserQA;password=HDuMy873JRFpkkU9;database=VDVI_Master");

		System.out.println("test");

		Statement sta = conn.createStatement();
		String Sql = "select dt01.DlrCode,vt01.VIN, vt01.VehGUID from DT01_Dealer as dt01 inner join VT01_DealerVehicles as vt01 on DT01.DlrGUID=VT01.DlrGUID where vt01.VIN=\'"
				+ sVin + "\' and dt01.DlrCode=\'" + dlrCode + "\'";
		String vGUID = "";
		ResultSet rs = sta.executeQuery(Sql);
		int icolumn = rs.getRow();
		while (rs.next()) {
			icolumn = rs.getRow();
			vGUID = rs.getString("VehGUID");
			System.out.println("Row =" + icolumn);
			System.out.println("Dealer Code = " + rs.getString("DlrCode") + "  VIN = " + rs.getString("VIN")
					+ "  VehGUID = " + rs.getString("VehGUID"));
		}
		if (icolumn == 1) {
			System.out.println("One VehGUID\n");
			// rs.getString("VehGUID");
			// String vGUID = rs.getString("VehGUID");
		} else {
			System.out.println("No any VehGUID or more than one\n");
			vGUID = "error!";
		}
		rs.close();
		sta.close();
		conn.close();
		return vGUID;
	}
	public static void StudyPriceDemoUITCs(WebDriver driver, String brw, String envment) 
			throws Exception {
		// Load environment parameters
		Properties prop = new Properties();
		// testprop.load(new FileInputStream("data/autopxConf.properties"));
		try {
			prop.load(StudyPriceDemoUIController.class.getClassLoader().getResourceAsStream("./data/studyPriceDemoUI.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		String env = prop.getProperty("StudyPrice.environment");
		String baseURL = prop.getProperty("StudyPrice.baseURL");
		String StudyPriceDemoUIURLEnv=  prop.getProperty(env+".StudyPriceDemoUIURLEnv");
		
		String versionNum = prop.getProperty("StudyPrice.versionNum");
		String envDevice = prop.getProperty("StudyPrice.envDevice");
		String envBrowser = prop.getProperty("StudyPrice.browser");
		String baseURL1 = StudyPriceDemoUIURLEnv+baseURL;
		int wt = Integer.parseInt(prop.getProperty("StudyPrice.waitTime"));
		// Initial
		String tc;
		String ptitle;
		Comlibs log =new Comlibs();
		log.rwExcel("", "*********Study Price Demo UI**********", "");
		StudyPRICEDemo studyPriceDemoP = new StudyPRICEDemo(driver);
		tc = "TCxxxx_01";
		studyPriceDemoP.clickVehicleInfoBtn(driver, tc);
		tc = "TCxxxx_021";
		studyPriceDemoP.clickVinBtn(driver, tc);
		studyPriceDemoP.verifyGoInActiveBtn(driver, tc);
		tc = "TCxxxx_03";
		studyPriceDemoP.clickVinArrow(driver, tc);
		tc = "TCxxxx_02";
		int vinCount=studyPriceDemoP.getVinCountFromVinDropList(driver, tc);
		//********************change here to large number***********************
		vinCount=2;
//		for (int i=1;i<=vinCount;i++) {
//			tc = "TCxxxx_03_"+i;
//			studyPriceDemoP.selectVinFromDropList(driver, i, tc);
//			log.Wait(wt);
//			studyPriceDemoP.clickGoActiveBtn(driver, tc);
//			log.Wait(wt);
//			log.GoBack(driver);
//			studyPriceDemoP.clickVinArrow(driver, tc);
//		}

		studyPriceDemoP.clickVehicleInfoBtn(driver, tc);
		String hintString="Enter Year Make Model";
		studyPriceDemoP.verifySearchHintText(driver, hintString, tc);
		tc = "TCxxxx_04";
//		studyPriceDemoP.inputYMM(driver, "2020", tc);// input ok, but no options showing
//		studyPriceDemoP.getVehicleCountFromVehicleList(driver, tc);// not ready.
		
		studyPriceDemoP.clickYearArrow(driver, tc);
		int yearListCount=studyPriceDemoP.getYearCountFromYearList(driver, tc);
		for (int i=3;i<=yearListCount;i++) {
			studyPriceDemoP.selectYearFromYearArrowList(driver, i, tc);
			studyPriceDemoP.clickMakeArrow(driver,tc);
			
			int makeListCount=studyPriceDemoP.getMakeCountFromMakeList(driver,tc);
			for (int j=1;j<=makeListCount;j++) {
				studyPriceDemoP.selectMakeFromMakeArrowList(driver, j, tc);
				studyPriceDemoP.clickMakeArrow(driver,tc);
//				//Model
//				studyPriceDemoP.clickModelArrow(driver,tc);
//				int modelListCount=studyPriceDemoP.getModelCountFromModelList(driver, tc);
//				for (int k=1;k<=modelListCount;k++) {
//				    studyPriceDemoP
//				    studyPriceDemoP
//				    studyPriceDemoP
				}

			}
//			studyPriceDemoP
//			studyPriceDemoP
//			studyPriceDemoP
			
		}

//		studyPriceDemoP
//		studyPriceDemoP
//		studyPriceDemoP
//		studyPriceDemoP
//		studyPriceDemoP
//		studyPriceDemoP
		
//		tc = "TCxxxx_03";
//		studyPriceDemoP
//		tc = "TCxxxx_03";
//		studyPriceDemoP
//		tc = "TCxxxx_03";
//		studyPriceDemoP
//		tc = "TCxxxx_03";
//		studyPriceDemoP
//		tc = "TCxxxx_03";
//		studyPriceDemoP
//		tc = "TCxxxx_03";
	
	public static void main(String[] args) throws Exception {
		// Load environment parameters
		Properties prop = new Properties();
		try {
			prop.load(StudyPriceDemoUIController.class.getClassLoader().getResourceAsStream("./data/studyPriceDemoUI.properties"));//"./main.properties";
		} catch (IOException e) {
			e.printStackTrace();
		}
		String env = prop.getProperty("StudyPrice.environment");
		String versionNum = prop.getProperty("StudyPrice.versionNum");
		String tBrowser = prop.getProperty("StudyPrice.browser");
		String envDevice = prop.getProperty("StudyPrice.envDevice");
		String onScreen = prop.getProperty("StudyPrice.onScreen");
		String baseURL = prop.getProperty("StudyPrice.baseURL");
		String StudyPriceDemoUIURLEnv = prop.getProperty(env + ".StudyPriceDemoUIURLEnv");

		// =fetchOneDemArrayFromPropFile(env+".StudyPriceVINs",prop);
		for (int i = 0; i < 1; i++) {
			System.out.println("Testing is started in " + env + "\n");
			// Initial
			Comlibs log = new Comlibs();
			final WebDriver driver;
			driver = log.drivers(tBrowser);// Firefox, Chrome
			driver.manage().deleteAllCookies();
			System.out.println("Test Browser = " + tBrowser + "\n");

			driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
			if (!tBrowser.equalsIgnoreCase("Chromexxxxxxxxx")) { 
				log.SelecBroswerResolution(driver, envDevice, onScreen);
			}
			log.rwExcel("", "****** Testing started ******" + (i + 1), "");
			log.rwExcel("", "Test Browser", tBrowser);
			log.rwExcel("", "Test Environment", env);

			loadURL(driver, StudyPriceDemoUIURLEnv+baseURL);

			//// 1. Study Price Demo UI Home page
			log.rwExcel("", "-----Study Price Demo UI Home page Testing started-----" + (i + 1), "");
			StudyPriceDemoUITCs(driver, tBrowser, env);

			

			log.rwExcel("", "****** Testing is complete ****** " + (i + 1), "");
			driver.close();
			System.out.println("Test is complete!!!   i = " + (i + 1));
		}
		return;
	}
}
