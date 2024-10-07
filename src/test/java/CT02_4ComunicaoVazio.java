import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.Duration;
import static java.lang.Thread.sleep;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
public class CT02_4ComunicaoVazio  {
    BufferedReader buffer;
    StringBuilder json;
    String linha;
    JsonParser parser;
    JsonObject jsonObject;
    ChromeOptions options;
    static WebDriver navegador;
    static WebDriverWait espera;

    @BeforeEach
    public void setUp() {
        try {
            // Lê o arquivo JSON usando um BufferedReader
            buffer = new BufferedReader(new FileReader("E:\\GitHub\\testesRp2\\src\\main\\resources\\CT02_4.json"));
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
    @DisplayName("Questionario com chatGPT")
    public void CT01() throws InterruptedException {
        Actions actions = new Actions(navegador);
        String usuario = jsonObject.get("usuario").getAsString();
        String senha = jsonObject.get("senha").getAsString();
        String nomeQuest = jsonObject.get("nomeQuest").getAsString();
        String qtdPerguntas = jsonObject.get("qtdPerguntas").getAsString();
        String qtdAlternativas = jsonObject.get("qtdAlternativas").getAsString();
        String tema = jsonObject.get("tema").getAsString();
        String unidade = jsonObject.get("unidade").getAsString();
        String turma = jsonObject.get("turma").getAsString();
        String disciplina = jsonObject.get("disciplina").getAsString();

        // Logar no site e chegar no menu chatGPT
        realizarLogin(navegador, espera, usuario, senha);
        //criar questionario com gpt
        criarQuestionario(navegador, espera, actions, nomeQuest, qtdPerguntas, qtdAlternativas, tema);
        //responder as questionario
        responderQuestionario(navegador, espera, actions, unidade, turma, disciplina, nomeQuest);
    }

    public void realizarLogin(WebDriver navegador, WebDriverWait espera, String usuario, String senha) throws InterruptedException {
        navegador.get(jsonObject.get("url").getAsString());
        espera.until(ExpectedConditions.visibilityOfElementLocated(By.name("login")));
        navegador.findElement(By.name("login")).sendKeys(usuario);
        navegador.findElement(By.name("password")).sendKeys(senha);
        sleep(500);
        navegador.findElement(By.name("btn_entrar")).click();
        espera.until(ExpectedConditions.visibilityOfElementLocated(By.tagName("body")));
        System.out.println("Título da nova janela: " + navegador.getTitle());
        sleep(1000);
        //chega no menu com chatgpt
        espera.until(d -> navegador.findElement(By.xpath("//*[@id=\"side-menu\"]/li[4]/a")));
        navegador.findElement(By.xpath("//*[@id=\"side-menu\"]/li[4]/a")).click();
        //Espera 2 segundos para verificar
        sleep(2000);
        // Compara se a url da página é a esperada
        try {
            Assertions.assertEquals("http://200.132.136.72/AIQuiz2/index.php?class=ChatGPTForm",
                    navegador.getCurrentUrl());
            System.out.println("Logado com sucesso");
        } catch (AssertionError e) {
            System.out.println("Erro no login");
        }
    }

    public void criarQuestionario(WebDriver navegador, WebDriverWait espera, Actions actions, String nomeQuest, String qtdPerguntas
            , String qtdAlternativas, String tema) throws InterruptedException {

        //poe o nome no questionario
        navegador.findElement((By.name("questionario"))).clear();
        navegador.findElement(By.name("questionario")).sendKeys(nomeQuest);

        // vai para o campo das perguntas
        sleep(500);
        navegador.findElement(By.name("qtdPerguntas")).click();
        sleep(500);
        actions.sendKeys(Keys.DELETE).perform();
        navegador.findElement((By.name("qtdPerguntas"))).sendKeys(qtdPerguntas);

        //vai para o quantidade de alternativas
        sleep(500);
        navegador.findElement((By.name("qtdAlternativas"))).click();
        sleep(500);
        actions.sendKeys(Keys.DELETE).perform();
        navegador.findElement((By.name("qtdAlternativas"))).sendKeys(qtdAlternativas);

        //chega no campo tema e limpa ele
        navegador.findElement((By.name("tema"))).clear();
        navegador.findElement((By.name("tema"))).sendKeys(tema);

        //elaborar perguntas
        sleep(500);
        espera.until(d -> navegador.findElement(By.name("btn_elabore_perguntas")));
        navegador.findElement(By.name("btn_elabore_perguntas")).click();

        //proximo
        sleep(4000);
        espera.until(d -> navegador.findElement(By.name("btn_next")));
        navegador.findElement(By.name("btn_next")).click();
        sleep(500);

        //verficao do teste
        sleep(500);
        WebElement mensagemErro = navegador.findElement(By.id("tbutton_btn_confirmar"));
        String textoMensagemErro = mensagemErro.getText();

        Assertions.assertEquals("Confirmar", textoMensagemErro);
        System.out.println("O teste foi gerado com sucesso.");
    }

    public void responderQuestionario(WebDriver navegador, WebDriverWait espera, Actions actions, String unidade, String turma, String disciplina, String nomeQuest) throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(navegador, Duration.ofSeconds(10));

        for (int k = 0; k < 3; k++) {
            // Espera até que o botão 'Selecionar' esteja clicável
            navegador.findElement(By.xpath("//span[contains(text(),'Selecionar')]")).click();
            sleep(500);

            if (k == 0) {
                sleep(500);
                WebElement inputField = navegador.findElement(By.xpath("/html/body/span/span/span[1]/input"));
                inputField.click();
                sleep(500);
                inputField.sendKeys(unidade);
                sleep(1000);
                // Envia a tecla ENTER
                inputField.sendKeys(Keys.ENTER);
                sleep(500);
            } else if (k == 1) {
                sleep(500);
                WebElement inputField = navegador.findElement(By.xpath("/html/body/span/span/span[1]/input"));
                inputField.click();
                sleep(500);
                inputField.sendKeys(turma);
                sleep(1000);
                // Envia a tecla ENTER
                inputField.sendKeys(Keys.ENTER);
                sleep(500);
            } else {
                sleep(500);
                WebElement inputField = navegador.findElement(By.xpath("/html/body/span/span/span[1]/input"));
                inputField.click();
                sleep(500);
                inputField.sendKeys(disciplina);
                sleep(1000);
                inputField.sendKeys(Keys.ENTER);
                sleep(500);
            }
        }
        //confirmar criacao
        espera.until(d -> navegador.findElement(By.id("tbutton_btn_confirmar"))).click();

        //verficao do teste
        sleep(500);
        WebElement mensagemErro = navegador.findElement(By.xpath("/html/body/div[2]/div/div/div[2]"));
        String textoMensagemErro = mensagemErro.getText();
        Assertions.assertEquals("O campo Comunicação é obrigatório.", textoMensagemErro);
        System.out.println("O campo Comunicação é obrigatório.");
    }
}
