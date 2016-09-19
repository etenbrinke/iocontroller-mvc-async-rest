package etenbrinke.iocontroller.mvc.async.rest.domain;

/**
 * Created by etenbrinke on 18/09/16.
 */
public class ResultWithOutputValue implements Result {

    private double outputValue;

    private Result result = new ResultWithSingleInputParameter();

    public ResultWithOutputValue(String inputParameter, String controllerCommand, String outputMessage, double outputValue) {
        result.setInputParameter(inputParameter);
        result.setControllerCommand(controllerCommand);
        result.setOutputMessage(outputMessage);
        this.outputValue = outputValue;
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

    public double getOutputValue() {
        return outputValue;
    }

    public void setOutputValue(double outputValue) {
        this.outputValue = outputValue;
    }
}
