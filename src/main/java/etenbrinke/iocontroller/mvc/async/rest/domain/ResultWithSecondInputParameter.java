package etenbrinke.iocontroller.mvc.async.rest.domain;

/**
 * Created by etenbrinke on 18/09/16.
 */
public class ResultWithSecondInputParameter implements Result {

    private String inputParameter2;

    private Result result = new ResultWithSingleInputParameter();

    public ResultWithSecondInputParameter(String inputParameter, String inputParameter2, String controllerCommand, String outputMessage) {
        result.setInputParameter(inputParameter);
        this.inputParameter2 = inputParameter2;
        result.setControllerCommand(controllerCommand);
        result.setOutputMessage(outputMessage);
    }

    public String getInputParameter2() {
        return inputParameter2;
    }

    public void setInputParameter2(String inputParameter2) {
        this.inputParameter2 = inputParameter2;
    }

    public String getInputParameter() {
        return result.getInputParameter();
    }

    public void setInputParameter(String inputParameter) {
        result.setInputParameter(inputParameter);
    }

    public String getControllerCommand() {
        return result.getControllerCommand();
    }

    public void setControllerCommand(String controllerCommand) {
        result.setControllerCommand(controllerCommand);
    }

    public String getOutputMessage() {
        return result.getOutputMessage();
    }

    public void setOutputMessage(String outputMessage) {
        result.setOutputMessage(outputMessage);
    }
}
