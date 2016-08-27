package etenbrinke.iocontroller.mvc.async.rest.domain;

/**
 * Created by etenbrinke on 05/09/16.
 */
public class ResultWithSingleInputParameter implements Result {

    private String inputParameter;
    private String controllerCommand;
    private String outputMessage;

    public ResultWithSingleInputParameter() {
    }

    public ResultWithSingleInputParameter(String inputParameter, String controllerCommand, String outputMessage) {
        this.inputParameter = inputParameter;
        this.controllerCommand = controllerCommand;
        this.outputMessage = outputMessage;
    }

    public String getInputParameter() {
        return inputParameter;
    }

    public void setInputParameter(String inputParameter) {
        this.inputParameter = inputParameter;
    }

    public String getControllerCommand() {
        return controllerCommand;
    }

    public void setControllerCommand(String controllerCommand) {
        this.controllerCommand = controllerCommand;
    }

    public String getOutputMessage() {
        return outputMessage;
    }

    public void setOutputMessage(String outputMessage) {
        this.outputMessage = outputMessage;
    }
}
