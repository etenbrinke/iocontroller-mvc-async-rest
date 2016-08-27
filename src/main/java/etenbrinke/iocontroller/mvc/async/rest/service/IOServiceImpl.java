package etenbrinke.iocontroller.mvc.async.rest.service;

import etenbrinke.iocontroller.mvc.async.rest.Settings;
import etenbrinke.iocontroller.mvc.async.rest.domain.ResultWithOutputValue;
import etenbrinke.iocontroller.mvc.async.rest.domain.ResultWithSecondInputParameter;
import etenbrinke.iocontroller.mvc.async.rest.domain.ResultWithSingleInputParameter;
import jssc.SerialPort;
import jssc.SerialPortException;
import jssc.SerialPortTimeoutException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * Created by etenbrinke on 05/12/15.
 */

@Service
@EnableConfigurationProperties(Settings.class)
public class IOServiceImpl implements IOService {

    private static final Logger log = LoggerFactory.getLogger(IOServiceImpl.class);
    private static final int WAIT_FOR_CONTROLLER_MS = 20;
    private static final int READ_TIMEOUT_MS = 30000;
    private static final String PARAMETER_OUT_OF_RANGE = "Parameter(s) out for range";
    private static final String READY_STATUS = "16";

    private SerialPort serialPort;

    private String serialDevice;
    private int ioAddress;

    /**
     Set serialDevice and ioAddress to enable serial communication
     ioAddress+1 will be used to disable serial communication
     @param serialDevice
     @param ioAddress device address 144,146,148,150
     */
    @Autowired
    public IOServiceImpl(@Value("${iocontroller.serialDevice}") String serialDevice, @Value("${iocontroller.ioAddress}") int ioAddress) {
        this.serialDevice = serialDevice;
        if (ioAddress == 144 || ioAddress == 146 || ioAddress == 148 || ioAddress == 150) {
            this.ioAddress = ioAddress;
        } else {
            log.info(PARAMETER_OUT_OF_RANGE);
        }
    }

    /**
     Open serial port and initialize controller
     @throws SerialPortException Represents Exception that might occur in Serial interface
     */
    @Override
    @PostConstruct
    public void openSerialPortAndInitializeController() throws SerialPortException {
        serialPort = new SerialPort(serialDevice);
        try {
            serialPort.openPort(); //Open serial port
            serialPort.setParams(SerialPort.BAUDRATE_9600, SerialPort.DATABITS_8, SerialPort.STOPBITS_2, SerialPort.PARITY_NONE); //Set parameters
            serialPort.writeInt((char) ioAddress);
            serialPort.writeByte((byte)0x18);
            serialPort.writeString("R");
            serialPort.writeByte((byte)0x0D);
            delay(WAIT_FOR_CONTROLLER_MS);
            log.info("Serial port "+serialDevice+" opened and controller reset");
        }
        catch (SerialPortException e) {
            log.info(e.getMessage());
            throw e;
        }
    }

    /**
     Close serial port
     @return Response
     @throws SerialPortException Represents Exception that might occur in Serial interface
     */
    @Override
    @PreDestroy
    public void closeSerialPort() throws SerialPortException {
        serialPort.closePort();
        log.info("Close serial port");
    }

    /**
     Reset Controller
     Controller command R
     @return Response
     @throws SerialPortException Represents Exception that might occur in Serial interface
     */
    @Override
    public ResultWithSingleInputParameter resetController() throws SerialPortException {
        final String controllerCommand = "R";
        serialPort.writeInt((char) ioAddress);
        serialPort.writeByte((byte)0x18);
        serialPort.writeString(controllerCommand);
        serialPort.writeByte((byte)0x0D);
        delay(WAIT_FOR_CONTROLLER_MS);
        String outputMessage = "Controller reset";
        log.info(outputMessage);
        return new ResultWithSingleInputParameter("", controllerCommand, outputMessage);
    }

    /**
     Set Local Mode
     Controller commands L,N
     @param localSwitch 1 to enable, 0 to disable
     @return Response
     @throws SerialPortException Represents Exception that might occur in Serial interface
     */
    // FIXME enable local mode hangsup program
    @Override
    public ResultWithSingleInputParameter setLocalMode(int localSwitch) throws SerialPortException {
        final String controllerCommand = "L,N";
        if (localSwitch == 0 || localSwitch == 1) {
            serialPort.writeInt((char) ioAddress);
            waitForControllerToBeReady();
            if (localSwitch == 1) {
                serialPort.writeString("L");
            } else {
                serialPort.writeString("N");
            }
            serialPort.writeByte((byte)0x0D);
            delay(WAIT_FOR_CONTROLLER_MS);
            serialPort.writeInt((char)(ioAddress+1));
            String outputMessage = "Local mode set to "+ localSwitch;
            log.info(outputMessage);
            return new ResultWithSingleInputParameter(String.valueOf(localSwitch), controllerCommand, outputMessage);
        } else {
            log.info(PARAMETER_OUT_OF_RANGE);
            return new ResultWithSingleInputParameter(String.valueOf(localSwitch),controllerCommand, PARAMETER_OUT_OF_RANGE);
        }
    }

    /**
     Set Echo Mode
     Controller commands X,Y
     @param echoSwitch 1 to enable, 0 to disable
     @return Response
     @throws SerialPortException Represents Exception that might occur in Serial interface
     */
    @Override
    public ResultWithSingleInputParameter setEchoMode(int echoSwitch) throws SerialPortException {
        final String controllerCommand = "X,Y";
        if (echoSwitch == 0 || echoSwitch == 1) {
            serialPort.writeInt((char) ioAddress);
            waitForControllerToBeReady();
            if (echoSwitch == 1) {
                serialPort.writeString("X");
            } else {
                serialPort.writeString("Y");
            }
            serialPort.writeByte((byte)0x0D);
            delay(WAIT_FOR_CONTROLLER_MS);
            serialPort.writeInt((char)(ioAddress+1));
            String outputMessage = "Echo mode set to "+ echoSwitch;
            log.info(outputMessage);
            return new ResultWithSingleInputParameter(String.valueOf(echoSwitch), controllerCommand, outputMessage);
        } else {
            log.info(PARAMETER_OUT_OF_RANGE);
            return new ResultWithSingleInputParameter(String.valueOf(echoSwitch),controllerCommand, PARAMETER_OUT_OF_RANGE);
        }
    }

    /**
     Set all digital outputs to high or low
     Controller commands C,D
     @param stateSwitch 1 set outputs to high, 0 to low
     @return Response
     @throws SerialPortException Represents Exception that might occur in Serial interface
     */
    @Override
    public ResultWithSingleInputParameter setAllDigitalOutputs(int stateSwitch) throws SerialPortException {
        final java.lang.String controllerCommand = "C,D";
        if (stateSwitch == 0 || stateSwitch == 1) {
            serialPort.writeInt((char) ioAddress);
            waitForControllerToBeReady();
            if (stateSwitch == 1) {
                serialPort.writeString("D");
            }
            else {
                serialPort.writeString("C");
            }
            serialPort.writeByte((byte)0x0D);
            delay(WAIT_FOR_CONTROLLER_MS);
            serialPort.writeInt((char)(ioAddress+1));
            String outputMessage = "All digital outputs set to "+ stateSwitch;
            log.info(outputMessage);
            return new ResultWithSingleInputParameter(String.valueOf(stateSwitch), controllerCommand, outputMessage);
        } else {
            log.info(PARAMETER_OUT_OF_RANGE);
            return new ResultWithSingleInputParameter(String.valueOf(stateSwitch),controllerCommand, PARAMETER_OUT_OF_RANGE);
        }
    }

    /**
     Set Block Connection
     Controller commands G,H
     @param block 0-3
     @param connectionSwitch 1 to make block connection, 0 do not make block connection
     @return Response
     @throws SerialPortException Represents Exception that might occur in Serial interface
     */
    @Override
    public ResultWithSecondInputParameter setBlockConnection(int block, int connectionSwitch) throws SerialPortException {
        final String controllerCommand = "G,H";
        if ((block >= 0 && block <= 3) && (connectionSwitch == 0 || connectionSwitch == 1)) {
            serialPort.writeInt((char) ioAddress);
            waitForControllerToBeReady();
            if (connectionSwitch == 1) {
                serialPort.writeString("G"+ block);
            }
            else { serialPort.writeString("H"+ block);
            }
            serialPort.writeByte((byte)0x0D);
            delay(WAIT_FOR_CONTROLLER_MS);
            serialPort.writeInt((char)(ioAddress+1));
            String outputMessage = "Block connection on block "+ block +" set to "+ connectionSwitch;
            log.info(outputMessage);
            return new ResultWithSecondInputParameter(String.valueOf(block), String.valueOf(connectionSwitch), controllerCommand, outputMessage);
        } else {
            log.info(PARAMETER_OUT_OF_RANGE);
            return new ResultWithSecondInputParameter(String.valueOf(block), String.valueOf(connectionSwitch),controllerCommand, PARAMETER_OUT_OF_RANGE);
        }
    }

    /**
     Read if Block is connected
     Controller command g
     @param block 0-3
     @return Response
     @throws SerialPortException Represents Exception that might occur in Serial interface
     */
    @Override
    public ResultWithOutputValue getBlockConnection(int block) throws SerialPortException, SerialPortTimeoutException {
        final String controllerCommand = "g";
        if (block >= 0 && block <= 3) {
            serialPort.writeInt((char) ioAddress);
            waitForControllerToBeReady();
            serialPort.writeString(controllerCommand+block);
            serialPort.writeByte((byte)0x0D);
            int connected = Integer.parseInt(serialPort.readString(2,READ_TIMEOUT_MS).trim());
            delay(WAIT_FOR_CONTROLLER_MS);
            serialPort.writeInt((char)(ioAddress+1));
            String outputMessage = "Block connection on block "+block+" is set to "+connected;
            log.info(outputMessage);
            return new ResultWithOutputValue(String.valueOf(block), controllerCommand, outputMessage, connected);
        } else {
            log.info(PARAMETER_OUT_OF_RANGE);
            return new ResultWithOutputValue(String.valueOf(block), controllerCommand, PARAMETER_OUT_OF_RANGE, -1);
        }
    }

    /**
     Set logical level on digital output
     Controller command A
     @param digitalOutput 0-31
     @param logicalLevel 0 or 1
     @return Response
     @throws SerialPortException Represents Exception that might occur in Serial interface
     */
    @Override
    public ResultWithSecondInputParameter setLogicalLevelDigitalOutput(int digitalOutput, int logicalLevel) throws SerialPortException {
        final String controllerCommand = "A";
        if ((digitalOutput >= 0 && digitalOutput <= 31) && (logicalLevel == 0 || logicalLevel == 1)) {
            serialPort.writeInt((char) ioAddress);
            waitForControllerToBeReady();
            serialPort.writeString(controllerCommand+digitalOutput+","+logicalLevel);
            serialPort.writeByte((byte)0x0D);
            delay(WAIT_FOR_CONTROLLER_MS);
            serialPort.writeInt((char)(ioAddress+1));
            String outputMessage = "Logical level "+ logicalLevel +" set on digital output "+ digitalOutput;
            log.info(outputMessage);
            return new ResultWithSecondInputParameter(String.valueOf(digitalOutput), String.valueOf(logicalLevel), controllerCommand, outputMessage);
        } else {
            log.info(PARAMETER_OUT_OF_RANGE);
            return new ResultWithSecondInputParameter(String.valueOf(digitalOutput), String.valueOf(logicalLevel), controllerCommand, PARAMETER_OUT_OF_RANGE);
        }
    }

    /**
     Get logical level on digital output
     Controller command a
     @param digitalOutput 0-31
     @return Response
     @throws SerialPortException Represents Exception that might occur in Serial interface
     */
    @Override
    public ResultWithOutputValue getLogicalLevelDigitalOutput(int digitalOutput) throws SerialPortException, SerialPortTimeoutException {
        final String controllerCommand = "a";
        if (digitalOutput >= 0 && digitalOutput <= 31) {
            serialPort.writeInt((char) ioAddress);
            waitForControllerToBeReady();
            serialPort.writeString(controllerCommand+digitalOutput);
            serialPort.writeByte((byte)0x0D);
            int logicalLevel = Integer.parseInt(serialPort.readString(2,READ_TIMEOUT_MS).trim());
            delay(WAIT_FOR_CONTROLLER_MS);
            serialPort.writeInt((char)(ioAddress+1));
            String outputMessage = "Logical level on digital output "+ digitalOutput +" is "+logicalLevel;
            log.info(outputMessage);
            return new ResultWithOutputValue(String.valueOf(digitalOutput), controllerCommand, outputMessage, logicalLevel);
        } else {
            log.info(PARAMETER_OUT_OF_RANGE);
            return new ResultWithOutputValue(String.valueOf(digitalOutput), controllerCommand, PARAMETER_OUT_OF_RANGE, -1);
        }
    }

    /**
     Set byte to digital output block
     Controller command B
     @param digitalOutputBlock 0-3
     @param byteValue 0-255
     @return Response
     @throws SerialPortException Represents Exception that might occur in Serial interface
     */
    @Override
    public ResultWithSecondInputParameter setByteDigitalOutputBlock(int digitalOutputBlock, int byteValue) throws SerialPortException {
        final String controllerCommand = "B";
        if ((digitalOutputBlock >= 0 && digitalOutputBlock <= 3) && (byteValue >=0 && byteValue <= 255)) {
            serialPort.writeInt((char) ioAddress);
            waitForControllerToBeReady();
            serialPort.writeString(controllerCommand+digitalOutputBlock+","+byteValue);
            serialPort.writeByte((byte)0x0D);
            delay(WAIT_FOR_CONTROLLER_MS);
            serialPort.writeInt((char)(ioAddress+1));
            String outputMessage = "Byte value "+byteValue+" set on digital output block "+ digitalOutputBlock;
            log.info(outputMessage);
            return new ResultWithSecondInputParameter(String.valueOf(digitalOutputBlock), String.valueOf(byteValue), controllerCommand, outputMessage);
        } else {
            log.info(PARAMETER_OUT_OF_RANGE);
            return new ResultWithSecondInputParameter(String.valueOf(digitalOutputBlock), String.valueOf(byteValue), controllerCommand, PARAMETER_OUT_OF_RANGE);
        }
    }

    /**
     Get byte on digital output block (8 outputs)
     Controller command b
     @param digitalOutputBlock 0-3
     @return Response
     @throws SerialPortException Represents Exception that might occur in Serial interface
     */
    @Override
    public ResultWithOutputValue getByteDigitalOutputBlock(int digitalOutputBlock) throws SerialPortException, SerialPortTimeoutException {
        final String controllerCommand = "b";
        if (digitalOutputBlock >= 0 && digitalOutputBlock <= 3) {
            serialPort.writeInt((char) ioAddress);
            waitForControllerToBeReady();
            serialPort.writeString(controllerCommand+digitalOutputBlock);
            serialPort.writeByte((byte)0x0D);
            int byteValue = Integer.parseInt(serialPort.readString(5,READ_TIMEOUT_MS).trim());
            delay(WAIT_FOR_CONTROLLER_MS);
            serialPort.writeInt((char)(ioAddress+1));
            String outputMessage = "Byte on digital block "+ digitalOutputBlock +" is "+byteValue;
            log.info(outputMessage);
            return new ResultWithOutputValue(String.valueOf(digitalOutputBlock), controllerCommand, outputMessage, byteValue);
        } else {
            log.info(PARAMETER_OUT_OF_RANGE);
            return new ResultWithOutputValue(String.valueOf(digitalOutputBlock), controllerCommand, PARAMETER_OUT_OF_RANGE, -1);
        }
    }

    /**
     Get byte from digital input block (8 inputs)
     Controller command f
     @param digitalInputBlock 0-3
     @return Response
     @throws SerialPortException Represents Exception that might occur in Serial interface
     */
    @Override
    public ResultWithOutputValue getByteDigitalInputBlock(int digitalInputBlock) throws SerialPortException, SerialPortTimeoutException {
        final String controllerCommand = "f";
        if (digitalInputBlock >= 0 && digitalInputBlock <= 3) {
            serialPort.writeInt((char) ioAddress);
            waitForControllerToBeReady();
            serialPort.writeString(controllerCommand+digitalInputBlock);
            serialPort.writeByte((byte)0x0D);
            int byteValue = Integer.parseInt(serialPort.readString(5,READ_TIMEOUT_MS).trim());
            delay(WAIT_FOR_CONTROLLER_MS);
            serialPort.writeInt((char)(ioAddress+1));
            String outputMessage = "Byte value on digital input block "+ digitalInputBlock +" is "+byteValue;
            log.info(outputMessage);
            return new ResultWithOutputValue(String.valueOf(digitalInputBlock), controllerCommand, outputMessage, byteValue);
        } else {
            log.info(PARAMETER_OUT_OF_RANGE);
            return new ResultWithOutputValue(String.valueOf(digitalInputBlock), controllerCommand, PARAMETER_OUT_OF_RANGE, -1);
        }
    }

    /**
     Get logical level on digital input
     Controller command e
     @param digitalInput 0-31
     @return logical level 0 or 1
     @throws SerialPortException Represents Exception that might occur in Serial interface
     */
    @Override
    public ResultWithOutputValue getLogicalLevelDigitalInput(int digitalInput) throws SerialPortException, SerialPortTimeoutException {
        final String controllerCommand = "e";
        if (digitalInput >= 0 && digitalInput <= 31) {
            serialPort.writeInt((char) ioAddress);
            waitForControllerToBeReady();
            serialPort.writeString(controllerCommand+digitalInput);
            serialPort.writeByte((byte)0x0D);
            int logicalLevel = Integer.parseInt(serialPort.readString(2,READ_TIMEOUT_MS).trim());
            delay(WAIT_FOR_CONTROLLER_MS);
            serialPort.writeInt((char)(ioAddress+1));
            String outputMessage = "Logical level on digital input "+ digitalInput +" is "+logicalLevel;
            return new ResultWithOutputValue(String.valueOf(digitalInput), controllerCommand, outputMessage, logicalLevel);
        } else {
            log.info(PARAMETER_OUT_OF_RANGE);
            return new ResultWithOutputValue(String.valueOf(digitalInput), controllerCommand, PARAMETER_OUT_OF_RANGE, -1);
        }
    }

    /**
     Set voltage to analog output
     Controller command U
     @param analogOutput 0-3
     @param voltage/100 in V 0-1023. 1023 equals 10.23 V
     @return Set 'voltage' V on analog output 'analogOutput'
     @throws SerialPortException Represents Exception that might occur in Serial interface
     */
    @Override
    public ResultWithSecondInputParameter setVoltageAnalogOutput(int analogOutput, int voltage) throws SerialPortException {
        final String controllerCommand = "U";
        if ((analogOutput >= 0 && analogOutput <= 3) && (voltage >= 0 && voltage <= 1023)) {
            serialPort.writeInt((char) ioAddress);
            waitForControllerToBeReady();
            serialPort.writeString(controllerCommand+analogOutput+","+ voltage);
            serialPort.writeByte((byte)0x0D);
            delay(WAIT_FOR_CONTROLLER_MS);
            serialPort.writeInt((char)(ioAddress+1));
            String outputMessage = "Set "+String.format("%.2f", (double)voltage/100)+" V on analog output "+analogOutput;
            log.info(outputMessage);
            return new ResultWithSecondInputParameter(String.valueOf(analogOutput), String.valueOf(voltage), controllerCommand, outputMessage);
        } else {
            log.info(PARAMETER_OUT_OF_RANGE);
            return new ResultWithSecondInputParameter(String.valueOf(analogOutput), String.valueOf(voltage), controllerCommand, PARAMETER_OUT_OF_RANGE);
        }
    }

    /**
     Get voltage on analog output
     Controller command u
     @param analogOutput 0-3
     @return voltage in V 0-10.23
     @throws SerialPortException Represents Exception that might occur in Serial interface
     */
    @Override
    public ResultWithOutputValue getVoltageAnalogOutput(int analogOutput) throws SerialPortException, SerialPortTimeoutException {
        final String controllerCommand = "u";
        if (analogOutput >= 0 || analogOutput <= 3) {
            serialPort.writeInt((char) ioAddress);
            waitForControllerToBeReady();
            serialPort.writeString(controllerCommand+analogOutput);
            serialPort.writeByte((byte)0x0D);
            double voltage = Double.parseDouble(serialPort.readString(6,READ_TIMEOUT_MS).trim());
            delay(WAIT_FOR_CONTROLLER_MS);
            serialPort.writeInt((char)(ioAddress+1));
            String outputMessage = "Voltage on analog output "+analogOutput+" is "+voltage+" V";
            log.info(outputMessage);
            return new ResultWithOutputValue(String.valueOf(analogOutput), controllerCommand, outputMessage, voltage);
        } else {
            log.info(PARAMETER_OUT_OF_RANGE);
            return new ResultWithOutputValue(String.valueOf(analogOutput), controllerCommand, PARAMETER_OUT_OF_RANGE, -1);
        }
    }

    /**
     Get voltage on analog input
     Controller command v
     @param analogInput 0-3
     @return voltage in V 0-10.23
     @throws SerialPortException Represents Exception that might occur in Serial interface
     */
    @Override
    public ResultWithOutputValue getVoltageAnalogInput(int analogInput) throws SerialPortException, SerialPortTimeoutException {
        final String controllerCommand = "v";
        if (analogInput >= 0 || analogInput <= 3) {
            serialPort.writeInt((char) ioAddress);
            waitForControllerToBeReady();
            serialPort.writeString(controllerCommand+analogInput);
            serialPort.writeByte((byte)0x0D);
            double voltage = Double.parseDouble(serialPort.readString(6,READ_TIMEOUT_MS).trim());
            delay(WAIT_FOR_CONTROLLER_MS);
            serialPort.writeInt((char)(ioAddress+1));
            String outputMessage = "Voltage on analog input "+analogInput+" is "+voltage+" V";
            log.info(outputMessage);
            return new ResultWithOutputValue(String.valueOf(analogInput), controllerCommand, outputMessage, voltage);
        } else {
            log.info(PARAMETER_OUT_OF_RANGE);
            return new ResultWithOutputValue(String.valueOf(analogInput), controllerCommand, PARAMETER_OUT_OF_RANGE, -1);
        }
    }

    /**
     delay
     @param ms time in millisecond
     */
    private static void delay(int ms) {
        try {
            log.debug("waiting " + ms + " ms");
            Thread.currentThread();
            Thread.sleep(ms); //sleep
        }
        catch(InterruptedException ie){
            log.info(ie.getMessage());
            //clean up stateSwitch
            Thread.currentThread().interrupt();
        }
    }

    /**
     Check if controller is ready for a new command to receive
     When controller sent a 16H status-byte it is ready
     @throws SerialPortException Represents Exception that might occur in Serial interface
     */
    private void waitForControllerToBeReady() throws SerialPortException {
        delay(WAIT_FOR_CONTROLLER_MS);
        String status = "";
        while (!READY_STATUS.equals(status))
        {   serialPort.writeInt((char)0);
            byte[] buffer = serialPort.readBytes(1);
            status = Integer.toHexString(buffer[0]);
            log.debug("Status received from controller : " + status + "H");
        }
    }
}
