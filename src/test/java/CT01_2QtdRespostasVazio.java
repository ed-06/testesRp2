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
import java.security.SecureRandom;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.Duration;
import static java.lang.Thread.sleep;

public class CT01_2QtdRespostasVazio {
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
            buffer = new BufferedReader(new FileReader("C:\\Users\\eduar\\Documents\\GitHub\\testesRp2\\src\\main\\resources\\CT01_2.json"));
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
        SecureRandom RANDOM = new SecureRandom();
        String urlPlataforma = jsonObject.get("url").getAsString();
        String usuario = jsonObject.get("usuario").getAsString();
        String senha = jsonObject.get("senha").getAsString();
        String nomeQuest = jsonObject.get("nomeQuest").getAsString();
        String qtdPerguntas = jsonObject.get("qtdPerguntas").getAsString();
        String qtdAlternativas = jsonObject.get("qtdAlternativas").getAsString();
        String tema = jsonObject.get("tema").getAsString();
        String disciplina = jsonObject.get("disciplina").getAsString();
        String resposta = jsonObject.get("resposta").getAsString();

        // Logar no site e chegar no menu chatGPT
        realizarLogin(navegador, espera, usuario, senha);
        //criar questionario com gpt
        criarQuestionario(navegador, espera, actions, nomeQuest, qtdPerguntas, qtdAlternativas, tema);
    }

    public void realizarLogin(WebDriver navegador, WebDriverWait espera, String usuario, String senha) throws InterruptedException {
        navegador.get(jsonObject.get("url").getAsString());
        espera.until(ExpectedConditions.visibilityOfElementLocated(By.name("login")));
        navegador.findElement(By.name("login")).sendKeys(usuario);
        navegador.findElement(By.name("password")).sendKeys(senha);
        navegador.findElement(By.name("btn_entrar")).click();
        espera.until(ExpectedConditions.visibilityOfElementLocated(By.tagName("body")));
        System.out.println("Título da nova janela: " + navegador.getTitle());
        sleep(1000);
        //chega no menu com chatgpt
        espera.until(d -> navegador.findElement(By.xpath("//*[@id=\"side-menu\"]/li[2]/a")));
        navegador.findElement(By.xpath("//*[@id=\"side-menu\"]/li[2]/a")).click();
        //Espera 2 segundos para verificar
        Thread.sleep(2000);
        // Compara se a url da página é a esperada
        try {
            Assertions.assertEquals("http://200.132.136.72/AIQuiz/index.php?class=ResponderListOnLine&previous_class=LoginForm",
                    navegador.getCurrentUrl());
            System.out.println("Logado com sucesso");
        } catch (AssertionError e) {
            System.out.println("Erro no login");
        }
    }

    public void criarQuestionario(WebDriver navegador, WebDriverWait espera, Actions actions, String nomeQuest, String qtdPerguntas
            , String qtdAlternativas, String tema) throws InterruptedException {
        espera.until(d -> navegador.findElement(By.name("questionario")));
        navegador.findElement(By.name("questionario")).click();

        //poe o nome no questionario
        navegador.findElement((By.name("questionario"))).clear();
        espera.until(d -> navegador.findElement(By.name("questionario")));
        navegador.findElement(By.name("questionario")).sendKeys(nomeQuest);

        // vai para o campo das perguntas
        sleep(500);
        actions.sendKeys(Keys.TAB).perform();

        //quantidade de perguntas
        espera.until(d -> navegador.findElement(By.name("qtdPerguntas")));
        navegador.findElement((By.name("qtdPerguntas"))).sendKeys(qtdPerguntas);

        //vai para o quantidade de alternaticas
        actions.sendKeys(Keys.TAB).perform();
        navegador.findElement((By.name("qtdAlternativas"))).clear();
        espera.until(d -> navegador.findElement(By.name("qtdAlternativas")));
        navegador.findElement((By.name("qtdAlternativas"))).sendKeys(qtdAlternativas);

        //chega no campo tema e limpa ele
        actions.sendKeys(Keys.TAB).perform();
        navegador.findElement((By.name("tema"))).clear();
        espera.until(d -> navegador.findElement(By.name("tema")));
        navegador.findElement((By.name("tema"))).sendKeys(tema);

        //elaborar perguntas
        sleep(500);
        espera.until(d -> navegador.findElement(By.name("btn_elabore_perguntas")));
        navegador.findElement(By.name("btn_elabore_perguntas")).click();

        sleep(4000);
        WebElement mensagemErro = navegador.findElement(By.xpath("/html/body/div[2]/div/div/div[2]"));
        String textoMensagemErro = mensagemErro.getText();
        Assertions.assertEquals("O campo Quantidade de Perguntas é obrigatório.", textoMensagemErro);
        System.out.println("O campo Quantidade de Perguntas é obrigatório.");
    }
}