
package genericUtility;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

public class ScreenshotsUtility {

    public static void captureScreenshot(
            WebDriver driver, String screenshotName) {

        try {
            Path folder = Paths.get("screenshots");
            Files.createDirectories(folder);

            String timestamp = LocalDateTime.now().format(
                DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")
            );

            File source = ((TakesScreenshot) driver)
                    .getScreenshotAs(OutputType.FILE);

            Path destination = folder.resolve(
                    screenshotName + "_" + timestamp + ".png"
            );

            Files.copy(source.toPath(), destination);

            System.out.println(
                    "Screenshot saved: " + destination.toAbsolutePath()
            );

        } catch (IOException e) {
            throw new RuntimeException(
                    "Unable to save screenshot: " + screenshotName, e
            );
        }
    }
}
