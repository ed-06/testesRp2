import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.github.bonigarcia.wdm.WebDriverManager;
import junit.framework.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.Set;
import java.util.ArrayList;

import static java.lang.Thread.sleep;

public class CT02ResponderOnlineRE {
    BufferedReader buffer;
    StringBuilder json;
    String linha;
    JsonParser parser;
    JsonObject jsonObject;
    ChromeOptions options;
    static WebDriver navegador;
    static Wait<WebDriver> espera;

    @BeforeEach
    public void setUp() {
        try {
            // Lê o arquivo JSON usando um BufferedReader
            buffer = new BufferedReader(new FileReader("C:\\Users\\eduar\\Documents\\teste\\src\\main\\resources\\CT-01_ResponderOnline.json"));
            json = new StringBuilder();
            while ((linha = buffer.readLine()) != null) {
                json.append(linha);
            }
            buffer.close();
            // Converte o conteúdo do arquivo JSON em um objeto JsonObject
            parser = new JsonParser();
            jsonObject = parser.parse(json.toString()).getAsJsonObject();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Define as opções do Chrome
        options = new ChromeOptions();
        options.addArguments("start-maximized");
        // Inicializa o WebDriver
        WebDriverManager.chromedriver().setup();
        navegador = new ChromeDriver(options);
        espera = new WebDriverWait(navegador, Duration.ofSeconds(50));
    }
    @Test
    @DisplayName("CT01 - Login Usuário")
    public void CT01() throws InterruptedException {
        Actions actions = new Actions(navegador);
        String urlPlataforma = jsonObject.get("url").getAsString();
        String usuario = jsonObject.get("usuario").getAsString();
        String senha = jsonObject.get("senha").getAsString();
        // Abrir a plataforma
        navegador.get(urlPlataforma);
        // Espera até o campo login aparecer
        espera.until(ExpectedConditions.visibilityOfElementLocated(By.name("login")));
        // Acha os campos e já preenche eles
        navegador.findElement(By.name("login")).sendKeys(usuario);
        navegador.findElement(By.name("password")).sendKeys(senha);
        navegador.findElement(By.name("btn_entrar")).click();
        // Armazena o identificador da janela original
        String originalWindow = navegador.getWindowHandle();
        // Espera até que algum elemento específico da nova janela seja encontrado
        espera.until(ExpectedConditions.visibilityOfElementLocated(By.tagName("body")));
        System.out.println("Título da nova janela: " + navegador.getTitle());
        // Acessa a funcionalidade de questionários
        espera.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"side-menu\"]/li[7]/a")));
        navegador.findElement(By.xpath("//*[@id=\"side-menu\"]/li[7]/a")).click();

        sleep(500);
        for (int i = 0; i < 6; i++) {
            actions.sendKeys(Keys.TAB).perform();
        }
        //procura o questionário
        sleep(250);
        espera.until(ExpectedConditions.visibilityOfElementLocated(By.name("id")));
        navegador.findElement(By.name("id")).sendKeys("541");
        actions.sendKeys(Keys.TAB).perform();

        sleep(250);
        espera.until(ExpectedConditions.visibilityOfElementLocated(By.name("titulo")));
        navegador.findElement(By.name("titulo")).sendKeys("Questionário XX84-fbnwfc");
        actions.sendKeys(Keys.TAB).perform();

        sleep(250);
        espera.until(ExpectedConditions.visibilityOfElementLocated(By.name("descricao")));
         navegador.findElement(By.name("descricao")).sendKeys("Questionário XXbhdvhbufo");

        sleep(250);
        espera.until(ExpectedConditions.visibilityOfElementLocated(By.name("btn_buscar")));
        navegador.findElement(By.name("btn_buscar")).click();

        sleep(5000);

        sleep(250);
        try {
            WebElement element = navegador.findElement(By.className("tdatagrid_cell"));
            if (element.getAttribute("class").contains("action")) {
                element.click();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        sleep(250);
        for (int i = 0; i < 2; i++) {
            actions.sendKeys(Keys.TAB).perform();
        }
        actions.sendKeys(Keys.ENTER).perform();
        //Responder o questionário
        sleep(500);
        espera.until(ExpectedConditions.visibilityOfElementLocated(By.name("btnLiberar")));
        navegador.findElement(By.name("btnLiberar")).click();

        //primeira questão
        sleep(5000);
        actions.sendKeys(Keys.TAB).perform();
        actions.sendKeys(Keys.TAB).perform();
        actions.sendKeys(Keys.ENTER).perform();

        sleep(500);
        espera.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[2]/div/div/div[3]/button")));
        navegador.findElement(By.xpath("/html/body/div[2]/div/div/div[3]/button")).click();


        //segunda questão
        sleep(500);
        espera.until(ExpectedConditions.visibilityOfElementLocated(By.name("btnLiberar")));
        navegador.findElement(By.name("btnLiberar")).click();

        sleep(500);
        actions.sendKeys(Keys.TAB).perform();
        actions.sendKeys(Keys.ENTER).perform();

        sleep(500);
        espera.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[2]/div/div/div[3]/button")));
        navegador.findElement(By.xpath("/html/body/div[2]/div/div/div[3]/button")).click();

        //terceira questão
        sleep(500);
        espera.until(ExpectedConditions.visibilityOfElementLocated(By.name("btnLiberar")));
        navegador.findElement(By.name("btnLiberar")).click();

        sleep(500);
        actions.sendKeys(Keys.TAB).perform();
        actions.sendKeys(Keys.ENTER).perform();

        sleep(500);
        espera.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[2]/div/div/div[3]/button")));
        navegador.findElement(By.xpath("/html/body/div[2]/div/div/div[3]/button")).click();

        //quarta questão
        sleep(500);
        espera.until(ExpectedConditions.visibilityOfElementLocated(By.name("btnLiberar")));
        navegador.findElement(By.name("btnLiberar")).click();

        sleep(500);
        actions.sendKeys(Keys.TAB).perform();
        actions.sendKeys(Keys.ENTER).perform();

        sleep(500);
        espera.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[2]/div/div/div[3]/button")));
        navegador.findElement(By.xpath("/html/body/div[2]/div/div/div[3]/button")).click();

        String retorno = navegador.findElement(By.className("modal-title")).getText();

        Assertions.assertEquals("Informação", retorno);
    }
}