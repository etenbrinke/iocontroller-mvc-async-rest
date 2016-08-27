package etenbrinke.iocontroller.mvc.async.rest.domain;

/**
 * Created by etenbrinke on 13/09/16.
 */
public interface Result {

    String getInputParameter();

    void setInputParameter(String inputParameter);

    String getControllerCommand();

    void setControllerCommand(String controllerCommand);

    String getOutputMessage();

    void setOutputMessage(String outputMessage);

}
