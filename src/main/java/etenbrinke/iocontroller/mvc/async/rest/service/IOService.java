package etenbrinke.iocontroller.mvc.async.rest.service;

import etenbrinke.iocontroller.mvc.async.rest.domain.ResultWithOutputValue;
import etenbrinke.iocontroller.mvc.async.rest.domain.ResultWithSecondInputParameter;
import etenbrinke.iocontroller.mvc.async.rest.domain.ResultWithSingleInputParameter;
import jssc.SerialPortException;
import jssc.SerialPortTimeoutException;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * Created by etenbrinke on 05/12/15.
 */

public interface IOService {

    @PostConstruct
    void openSerialPortAndInitializeController() throws SerialPortException;

    @PreDestroy
    void closeSerialPort() throws SerialPortException;

    ResultWithSingleInputParameter resetController() throws SerialPortException;

    ResultWithSingleInputParameter setLocalMode(int localSwitch) throws SerialPortException;

    ResultWithSingleInputParameter setEchoMode(int echoSwitch) throws SerialPortException;

    ResultWithSingleInputParameter setAllDigitalOutputs(int stateSwitch) throws SerialPortException;

    ResultWithSecondInputParameter setBlockConnection(int block, int connectionSwitch) throws SerialPortException;

    ResultWithOutputValue getBlockConnection(int block) throws SerialPortException, SerialPortTimeoutException;

    ResultWithSecondInputParameter setLogicalLevelDigitalOutput(int digitalOutput, int level) throws SerialPortException;

    ResultWithOutputValue getLogicalLevelDigitalOutput(int digitalOutput) throws SerialPortException, SerialPortTimeoutException;

    ResultWithSecondInputParameter setByteDigitalOutputBlock(int digitalOutputBlock, int byteValue) throws SerialPortException;

    ResultWithOutputValue getByteDigitalOutputBlock(int digitalOutputBlock) throws SerialPortException, SerialPortTimeoutException;

    ResultWithOutputValue getByteDigitalInputBlock(int digitalInputBlock) throws SerialPortException, SerialPortTimeoutException;

    ResultWithOutputValue getLogicalLevelDigitalInput(int digitalInput) throws SerialPortException, SerialPortTimeoutException;

    ResultWithSecondInputParameter setVoltageAnalogOutput(int analogOutput, int voltage) throws SerialPortException;

    ResultWithOutputValue getVoltageAnalogOutput(int analogOutput) throws SerialPortException, SerialPortTimeoutException;

    ResultWithOutputValue getVoltageAnalogInput(int analogInput) throws SerialPortException, SerialPortTimeoutException;
}
