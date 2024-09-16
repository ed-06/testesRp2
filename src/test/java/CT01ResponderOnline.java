import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.util.Random;
import java.security.SecureRandom;


import javax.swing.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.Duration;

import static java.lang.Thread.sleep;

public class CT01ResponderOnline {
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
    @DisplayName("CT01 - Login Usuário Válido")
    public void CT01() throws InterruptedException {
        Actions actions = new Actions(navegador);
        SecureRandom RANDOM = new SecureRandom();
        // Obtendo os dados do arquivo JSON
        String urlPlataforma = jsonObject.get("url").getAsString();
        String usuario = jsonObject.get("usuario").getAsString();
        String senha = jsonObject.get("senha").getAsString();
        String questionario;
        //gerarStringAleatoria();

        // Abrir a plataforma
        navegador.get(urlPlataforma);

        // Espera até o campo login aparecer
        espera.until(d -> navegador.findElement(By.name("login")));

        // Acha os campos e já preenche eles
        navegador.findElement(By.name("login")).sendKeys(usuario);
        navegador.findElement(By.name("password")).sendKeys(senha);

        // Clica no botão de login
        navegador.findElement(By.name("btn_entrar")).click();

        espera.until(d -> navegador.findElement(By.xpath("//*[@id=\"side-menu\"]/li[2]/a")));
        navegador.findElement(By.xpath("//*[@id=\"side-menu\"]/li[2]/a")).click();

        //limpa o campo quest
        sleep(2000);
        for (int i = 0; i < 10; i++) {
            actions.sendKeys(Keys.TAB).perform();
        }

        sleep(2000);
        for (int i = 0; i < 20; i++) {
            actions.sendKeys(Keys.BACK_SPACE).perform();
        }


        //questionario nome
        espera.until(d -> navegador.findElement(By.name("questionario")));
        navegador.findElement(By.name("questionario")).sendKeys("questionario");

        sleep(2000);
        for (int i = 0; i < 1; i++) {
            actions.sendKeys(Keys.TAB).perform();
        }

        //quantidade de perguntas
        espera.until(d -> navegador.findElement(By.name("qtdPerguntas")));
        navegador.findElement((By.name("qtdPerguntas"))).sendKeys("4");

        sleep(2000);
        for (int i = 0; i < 2; i++) {
            actions.sendKeys(Keys.TAB).perform();
        }
        navegador.findElement((By.name("tema"))).clear();
        //tema
        espera.until(d -> navegador.findElement(By.name("tema")));
        navegador.findElement((By.name("tema"))).sendKeys("Futebol");

        sleep(500);
        //elaborar perguntas
        espera.until(d -> navegador.findElement(By.name("btn_elabore_perguntas")));
        navegador.findElement(By.name("btn_elabore_perguntas")).click();

        sleep(10000);
        //proximo
        espera.until(d -> navegador.findElement(By.name("btn_próximo")));
        navegador.findElement(By.name("btn_próximo")).click();

        //disciplina
        espera.until(d -> navegador.findElement(By.name("disciplina")));
        navegador.findElement(By.name("disciplina")).click();
        //sincrona
        sleep(1000);
        actions.sendKeys(Keys.ARROW_DOWN);
        sleep(100);
        actions.sendKeys(Keys.ENTER);

        espera.until(d -> navegador.findElement(By.name("comunicacao")));
        navegador.findElement(By.name("comunicacao")).click();
        sleep(1000);
        actions.sendKeys(Keys.ARROW_DOWN);
        sleep(100);
        actions.sendKeys(Keys.ENTER);

        espera.until(d -> navegador.findElement(By.name("comunicacao")));
        navegador.findElement(By.name("comunicacao")).click();

        //escolhas 1
        espera.until(d -> navegador.findElement(By.name("radio1")));
        navegador.findElement(By.name("radio1")).click();
        //2
        espera.until(d -> navegador.findElement(By.name("radio2")));
        navegador.findElement(By.name("radio2")).click();
        //3
        espera.until(d -> navegador.findElement(By.name("radio3")));
        navegador.findElement(By.name("radio3")).click();
        //4
        espera.until(d -> navegador.findElement(By.name("radio4")));
        navegador.findElement(By.name("radio4")).click();
/*
        public static void gerarStringAleatoria() {
            int comprimento = 8;
            String resultado = "";
            String CARACTERES = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

            for (int i = 0; i < comprimento; i++) {
                int indice = RANDOM.nextInt(CARACTERES.length());
                resultado += CARACTERES.charAt(indice);
            }

            int numeroAleatorio = RANDOM.nextInt(10000);

            String resultadoFinal = resultado + numeroAleatorio;

 */
        }
    }
