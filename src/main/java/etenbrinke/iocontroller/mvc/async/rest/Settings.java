package etenbrinke.iocontroller.mvc.async.rest;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Created by etenbrinke on 05/12/15.
 */

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "iocontroller")
public class Settings {

    private String serialDevice;
    private int ioAddress;
    private String version;

    public String getSerialDevice() {
        return serialDevice;
    }

    public void setSerialDevice(String serialDevice) {
        this.serialDevice = serialDevice;
    }

    public int getIoAddress() {
        return ioAddress;
    }

    public void setIoAddress(int ioAddress) {
        this.ioAddress = ioAddress;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
