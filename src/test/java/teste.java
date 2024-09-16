import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class teste {
    WebDriver navegador = new ChromeDriver();
    public void responderOnline(){
        navegador.get("http://200.132.136.72/AIQuiz/index.php?class=MultiStepMultiFormView");
        WebElement link = navegador.findElement(By.xpath("/html/body/div/aside/section/ul/li[2]/a"));
    }
}


