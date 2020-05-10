package nnpia.seme

import geb.Browser
import nnpia.seme.dao.UserRepository
import nnpia.seme.model.User
import org.junit.jupiter.api.Test
import org.junit.runner.RunWith
import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.WebDriverWait
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.test.context.junit4.SpringRunner

import java.sql.Timestamp

import static org.junit.jupiter.api.Assertions.assertEquals

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT
)
class UISeleniumTests {

  @Autowired
  private BCryptPasswordEncoder bcryptEncoder;

  @Autowired
  private Creator creator;

  @Test
  void testUpce() {
    System.setProperty("webdriver.chrome.driver", "C:/Users/Spektrom/Downloads/chromedriver81.exe");

    WebDriver driver = new ChromeDriver();
    driver.get("http://www.upce.cz/");
    assertEquals(driver.title, "Univerzita Pardubice")
    driver.findElement(By.linkText("POPLATKY")).click()
  }

  @Test
  void testPrihlaseni() {
    System.setProperty("webdriver.chrome.driver", "C:/Users/Spektrom/Downloads/chromedriver81.exe");

    User user = new User();
    user.setUsername("testLog");
    Date date= new Date();
    user.setCreate_time(new Timestamp(date.getTime()));//Creater neumi vytvorit timestamp
    user.setPassword(bcryptEncoder.encode("123456"))
    //creator.saveEntity(user);

    WebDriver driver = new ChromeDriver();
    driver.get("http://localhost:3000/");
    assertEquals(driver.title, "Seme app")

    driver.findElement(By.linkText("Login")).click()

    driver.findElement(By.name("username")).sendKeys("testLog")
    driver.findElement(By.name("password")).sendKeys("123456")
    driver.findElement(By.className("btn-primary")).click()

    WebDriverWait wait = new WebDriverWait(driver, 3);

    wait.until(ExpectedConditions.textToBe(By.xpath("//*[@id=\"root\"]/div/div/h1"),'Main page for logged user' ))
    wait.until(ExpectedConditions.textToBe(By.xpath("//*[@id=\"root\"]/div/div/h2"),'Welcome testLog' ))

  }

  @Test
  void testPrihlaseniFail() {
    System.setProperty("webdriver.chrome.driver", "C:/Users/Spektrom/Downloads/chromedriver81.exe");

    User user = new User();
    user.setUsername("testLogF");
    Date date= new Date();
    user.setCreate_time(new Timestamp(date.getTime()));//Creater neumi vytvorit timestamp
    user.setPassword(bcryptEncoder.encode("123456"))
    //creator.saveEntity(user);

    WebDriver driver = new ChromeDriver();
    driver.get("http://localhost:3000/");
    assertEquals(driver.title, "Seme app")

    driver.findElement(By.linkText("Login")).click()

    driver.findElement(By.name("username")).sendKeys("testLogF")
    driver.findElement(By.name("password")).sendKeys("654321")
    driver.findElement(By.className("btn-primary")).click()

    WebDriverWait wait = new WebDriverWait(driver, 3);

    wait.until(ExpectedConditions.textToBe(By.xpath("//*[@id=\"root\"]/div/div/h3"),'Login' ))
    wait.until(ExpectedConditions.textToBe(By.xpath("//*[@id=\"root\"]/div/div/form/h2"),'Bad credentials' ))

  }

  @Test
  void testForm() {
    System.setProperty("webdriver.chrome.driver", "C:/Users/Spektrom/Downloads/chromedriver81.exe");

    User user = new User();
    user.setUsername("testLogForm");
    Date date= new Date();
    user.setCreate_time(new Timestamp(date.getTime()));//Creater neumi vytvorit timestamp
    //creator.saveEntity(user);

    WebDriver driver = new ChromeDriver();
    driver.get("http://localhost:3000/");
    assertEquals(driver.title, "Seme app")

    driver.findElement(By.linkText("Sign Up")).click()

    driver.findElement(By.name("username")).sendKeys("testLogForm")
    driver.findElement(By.name("email")).sendKeys("asd@asd.cy")
    driver.findElement(By.name("password")).sendKeys("654321")
    driver.findElement(By.className("btn-primary")).click()

    WebDriverWait wait = new WebDriverWait(driver, 3);

    wait.until(ExpectedConditions.textToBe(By.xpath("//*[@id=\"root\"]/div/div/h3"),'Registration' ))
    wait.until(ExpectedConditions.textToBe(By.xpath("//*[@id=\"root\"]/div/div/form/h2"),'Username already exist.' ))

  }


}