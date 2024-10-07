import com.google.gson.JsonObject;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
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
import org.openqa.selenium.chromium.ChromiumDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.Duration;

import static java.lang.Thread.sleep;

public class CT03ResponderOnline {
    private JsonObject jsonObject;
    private ChromiumDriver driver;
    private ChromeOptions options;
    private static WebDriver navegador;
    private static WebDriver navegador2;
    private static WebDriverWait espera;
    private static WebDriverWait espera2;

    @BeforeEach
    public void setUp() {
        // Lendo o JSON
        try {
            BufferedReader buffer = new BufferedReader(new FileReader("E:\\GitHub\\testesRp2\\src\\main\\resources\\CT03.json"));
            StringBuilder json = new StringBuilder();
            String linha;
            while ((linha = buffer.readLine()) != null) {
                json.append(linha);
            }
            JsonElement element = JsonParser.parseString(json.toString());
            jsonObject = element.getAsJsonObject();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Opções do Chrome
        options = new ChromeOptions();
        options.addArguments("start-maximized");

        // Navegador 1
        WebDriverManager.chromedriver().setup();
        navegador = new ChromeDriver(options);
        espera = new WebDriverWait(navegador, Duration.ofSeconds(50));

        // Navegador 2
        navegador2 = new ChromeDriver(options);
        espera2 = new WebDriverWait(navegador2, Duration.ofSeconds(50));
    }

    @Test
    @DisplayName("ResponderOnline")
    public void CT02() throws InterruptedException {
        Actions actions = new Actions(navegador);
        String usuario = jsonObject.get("usuario").getAsString();
        String senha = jsonObject.get("senha").getAsString();
        String id = jsonObject.get("id").getAsString();
        String questName = jsonObject.get("questName").getAsString();
        String unidade = jsonObject.get("unidade").getAsString();
        String turma = jsonObject.get("turma").getAsString();
        String disciplina = jsonObject.get("disciplina").getAsString();

        // LOGIN
        realizarLoginAcharQuest(navegador, espera, usuario, senha);
        realizarLoginResponder(navegador2, espera2, usuario, senha);

        //PROCURAR QUESTIONARIO
        procurarQuestionario(navegador, espera, id, questName, unidade, turma, disciplina);

        //RESPONDER QUESTONLINE
        responderQuestionario(navegador, espera, navegador2, espera2);

    }

    // Método para realizar login
    public void realizarLoginAcharQuest(WebDriver navegador, WebDriverWait espera, String usuario, String senha) throws InterruptedException {
        navegador.get(jsonObject.get("url").getAsString());
        sleep(500);
        espera.until(ExpectedConditions.visibilityOfElementLocated(By.name("login")));
        navegador.findElement(By.name("login")).sendKeys(usuario);
        navegador.findElement(By.name("password")).sendKeys(senha);
        sleep(500);
        navegador.findElement(By.name("btn_entrar")).click();
        espera.until(ExpectedConditions.visibilityOfElementLocated(By.tagName("body")));
        System.out.println("Título da nova janela: " + navegador.getTitle());

        // Navegar para o menu ChatGPT
        espera.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"side-menu\"]/li[8]/a")));
        navegador.findElement(By.xpath("//*[@id=\"side-menu\"]/li[8]/a")).click();

        // Verificar URL
        sleep(4000);
        Assertions.assertEquals("http://200.132.136.72/AIQuiz2/index.php?class=ResponderOnLineList",
                navegador.getCurrentUrl(), "Erro no login");
    }

    // Método para realizar o login do responder
    public void realizarLoginResponder(WebDriver navegador2, WebDriverWait espera2, String usuario, String senha) throws InterruptedException {
        String urlPin = jsonObject.get("url").getAsString();
        navegador2.get(jsonObject.get("urlPin").getAsString());
        espera2.until(ExpectedConditions.visibilityOfElementLocated(By.name("login")));
        navegador2.findElement(By.name("login")).sendKeys(usuario);
        navegador2.findElement(By.name("password")).sendKeys(senha);
        espera.until(d -> navegador2.findElement(By.name("btn_entrar"))).click();
        espera2.until(ExpectedConditions.visibilityOfElementLocated(By.tagName("body")));
        System.out.println("Título da nova janela: " + navegador2.getTitle());

        // Navegar para o menu ChatGPT
        espera2.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"side-menu\"]/li[9]/a")));
        navegador2.findElement(By.xpath("//*[@id=\"side-menu\"]/li[9]/a")).click();

        sleep(4000);
        Assertions.assertEquals("http://200.132.136.72/AIQuiz2/index.php?class=PublicPIN",
                navegador2.getCurrentUrl(), "Erro no login 2");
    }

    public void procurarQuestionario(WebDriver navegador, WebDriverWait espera, String id, String questName, String unidade, String turma, String disciplina) throws InterruptedException {
        Actions actions = new Actions(navegador);
        navegador.findElement(By.xpath("//*[@id=\"adianti_div_content\"]/div/div/div[2]/div/div[1]/div[2]/a[2]")).click();

        espera.until(d -> navegador.findElement(By.name("id"))).click();
        // navegador.findElement((By.name("id"))).sendKeys(id);
        //sleep(500);
        actions.sendKeys(Keys.TAB).perform();

        sleep(500);
        actions.sendKeys(questName).perform();
        sleep(500);

        espera.until(d -> navegador.findElement(By.name("unit"))).click();
        navegador.findElement((By.name("unit"))).sendKeys(unidade);
        sleep(500);

        //espera.until(d -> navegador.findElement(By.name("turma"))).click();
        //navegador.findElement((By.name("turma"))).sendKeys(turma);
        //sleep(500);

        //espera.until(d -> navegador.findElement(By.name("disciplina"))).click();
        //navegador.findElement((By.name("disciplina"))).sendKeys(disciplina);
        // sleep(500);

        espera.until(d -> navegador.findElement(By.name("btn_buscar"))).click();
        sleep(1000);

        for (int i = 0; i < 23; i++) {
            actions.sendKeys(Keys.TAB).perform();
            sleep(150);
        }
        sleep(1000);
        actions.sendKeys(Keys.ENTER).perform();

        //Teste para ver se entrou no questionario
        sleep(1000);
        WebElement mensagemErro = navegador.findElement(By.id("lblAguardando"));
        String textoMensagemErro = mensagemErro.getText();
        Assertions.assertEquals("Aguardando jogadores...", textoMensagemErro);
        System.out.println("Quiz iniciado com sucesso");
    }

    public void responderQuestionario(WebDriver navegador, WebDriverWait espera, WebDriver navegador2, WebDriverWait espera2) throws InterruptedException {
        Actions actions = new Actions(navegador);
        Actions actions2 = new Actions(navegador2);

        //pega o PIN
        WebElement pinElement = navegador.findElement(By.name("randomPIN"));
        String pinValue = pinElement.getText();
        String pin = pinValue.replace(" ", "");
        System.out.println("O PIN é: " + pin);

        sleep(500);

        espera2.until(d -> navegador2.findElement(By.name("pin"))).sendKeys(pin);
        espera2.until(d -> navegador2.findElement(By.name("btn_inserir"))).click();
        sleep(10000);
        espera2.until(d -> navegador2.findElement(By.name("btn_ok"))).click();
        sleep(1000);

        //iniciar quiz
        espera.until(d -> navegador.findElement(By.id("btnIniciar"))).click();
        sleep(250);

        for (int i = 0; i < 4; i++) {
            sleep(1000);
            espera.until(d -> navegador.findElement(By.id("btnLiberar"))).click();
            sleep(1000);
            actions2.sendKeys(Keys.TAB).perform();
            actions2.sendKeys(Keys.ENTER).perform();

            sleep(25000);
        }
        //Verificação teste navegador 1
        sleep(5000);
        WebElement mensagemErro = navegador.findElement(By.id("closeButton"));
        String textoMensagemErro = mensagemErro.getText();
        Assertions.assertEquals("Fechar", textoMensagemErro);
        System.out.println("O testo foi liberado da forma correto");

        //Verificação teste navegador 2
        sleep(500);
        WebElement mensagemErro2 = navegador2.findElement(By.id("closeButton"));
        String textoMensagemErro2 = mensagemErro2.getText();
        Assertions.assertEquals("Fechar", textoMensagemErro2);
        System.out.println("O testo foi respondido com sucesso");
    }
}

