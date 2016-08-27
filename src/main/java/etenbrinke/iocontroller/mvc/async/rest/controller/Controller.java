package etenbrinke.iocontroller.mvc.async.rest.controller;

import etenbrinke.iocontroller.mvc.async.rest.domain.ResultWithOutputValue;
import etenbrinke.iocontroller.mvc.async.rest.domain.ResultWithSecondInputParameter;
import etenbrinke.iocontroller.mvc.async.rest.domain.ResultWithSingleInputParameter;
import etenbrinke.iocontroller.mvc.async.rest.service.IOServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import jssc.SerialPortException;
import jssc.SerialPortTimeoutException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.concurrent.CompletableFuture;

/**
 * Created by etenbrinke on 05/12/15.
 */

@RestController
@Api(value="ioService")
@RequestMapping("/ioService")
public class Controller {
    private final IOServiceImpl ioServiceImpl;

    @Autowired
    public Controller(IOServiceImpl ioServiceImpl) {
        this.ioServiceImpl = ioServiceImpl;
    }

    /**
     Sample usage: curl "http://localhost:8080/ioService/version"
     */
    @ApiOperation(value = "Shows version of IoController Async REST API")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Version of IoController Async REST API") })
    @RequestMapping(value = "/version", method = RequestMethod.GET, produces = "test/html")
    public String version(@Value("${iocontroller.version}") String version) {
        return "IoController Async REST API build with Spring MVC version "+version;
    }

    /**
     Sample usage: curl "http://localhost:8080/ioService/reset"
     */
    @ApiOperation(value = "Reset Controller. Controller command R")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Controller reset") })
    @RequestMapping(value = "/reset", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public DeferredResult<ResultWithSingleInputParameter> reset() {
        DeferredResult<ResultWithSingleInputParameter> deferredResult = new DeferredResult<>();
        CompletableFuture.supplyAsync(() -> {
            try {
                return ioServiceImpl.resetController();
            } catch (SerialPortException e) {
                throw new RuntimeException(e);
            }
        }).whenCompleteAsync((result, throwable) -> deferredResult.setResult(result));
        return deferredResult;
    }

    /**
     Sample usage: curl "http://localhost:8080/ioService/setLocalMode?&local=1"
     */
    @ApiOperation(value = "Set Local Mode. Controller commands L,N")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Local mode set to 'local mode'") })
    @RequestMapping(value = "/setLocalMode", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public DeferredResult<ResultWithSingleInputParameter> setLocalMode(
            @ApiParam(value = "1 to enable, 0 to disable", required = true) @RequestParam(value = "localSwitch", required = true) int localSwitch) {
        DeferredResult<ResultWithSingleInputParameter> deferredResult = new DeferredResult<>();
        CompletableFuture.supplyAsync(() -> {
            try {
                return ioServiceImpl.setLocalMode(localSwitch);
            } catch (SerialPortException e) {
                throw new RuntimeException(e);
            }
        }).whenCompleteAsync((result, throwable) -> deferredResult.setResult(result));
        return deferredResult;
    }

    /**
     Sample usage: curl "http://localhost:8080/ioService/setEchoMode&echo=0"
     */
    @ApiOperation(value = "Set Echo Mode. Controller commands X,Y")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Echo Mode set to 'echo mode'") })
    @RequestMapping(value = "/setEchoMode", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public DeferredResult<ResultWithSingleInputParameter> setEchoMode(
            @ApiParam(value = "1 to enable, 0 to disable"
                    + ""
                    + "", required = true) @RequestParam(value = "echoSwitch", required = true) int echoSwitch) {
        DeferredResult<ResultWithSingleInputParameter> deferredResult = new DeferredResult<>();
        CompletableFuture.supplyAsync(() -> {
            try {
                return ioServiceImpl.setEchoMode(echoSwitch);
            } catch (SerialPortException e) {
                throw new RuntimeException(e);
            }
        }).whenCompleteAsync((result, throwable) -> deferredResult.setResult(result));
        return deferredResult;
    }

    /**
     Sample usage: curl "http://localhost:8080/ioService/setAllDigitalOutputs?&state=1"
     */
    @ApiOperation(value = "Set all digital outputs to high or low. Controller commands C,D")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "All digital outputs set to 'stateSwitch'") })
    @RequestMapping(value = "/setAllDigitalOutputs", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public DeferredResult<ResultWithSingleInputParameter> setAllDigitalOutputs(
            @ApiParam(value = "1 set outputs to high, 0 to low", required = true) @RequestParam(value = "stateSwitch", required = true) int stateSwitch) {
        DeferredResult<ResultWithSingleInputParameter> deferredResult = new DeferredResult<>();
        CompletableFuture.supplyAsync(() -> {
            try {
                return ioServiceImpl.setAllDigitalOutputs(stateSwitch);
            } catch (SerialPortException e) {
                throw new RuntimeException(e);
            }
        }).whenCompleteAsync((result, throwable) -> deferredResult.setResult(result));
        return deferredResult;
    }

    /**
     Sample usage: curl "http://localhost:8080/ioService/setBlockConnection?&block=0&connection=1"
     */
    @ApiOperation(value = "Set Block Connection. Controller commands G,H")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Block Connection on 'block' set to 'connection'") })
    @RequestMapping(value = "/setBlockConnection", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public DeferredResult<ResultWithSecondInputParameter> setBlockConnection(
            @ApiParam(value = "block 0-3", required = true) @RequestParam(value = "block", required = true) int block,
            @ApiParam(value = "1 to make block connection, 0 do not make block connection", required = true) @RequestParam(value = "connection", required = true) int connection) {
        DeferredResult<ResultWithSecondInputParameter> deferredResult = new DeferredResult<>();
        CompletableFuture.supplyAsync(() -> {
            try {
                return ioServiceImpl.setBlockConnection(block, connection);
            } catch (SerialPortException e) {
                throw new RuntimeException(e);
            }
        }).whenCompleteAsync((result, throwable) -> deferredResult.setResult(result));
        return deferredResult;
    }

    /**
     Sample usage: curl "http://localhost:8080/ioService/getBlockConnection?&block=0"
     */
    @ApiOperation(value = "Get if Block is connected. Controller command g")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Block connection on 'block number' is set to 0 (not connected) or 1 (connected)") })
    @RequestMapping(value = "/getBlockConnection", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public DeferredResult<ResultWithOutputValue> getBlockConnection(
            @ApiParam(value = "block 0-3", required = true) @RequestParam(value = "block", required = true) int block) {
        DeferredResult<ResultWithOutputValue> deferredResult = new DeferredResult<>();
        CompletableFuture.supplyAsync(() -> {
            try {
                return ioServiceImpl.getBlockConnection(block);
            } catch (SerialPortException | SerialPortTimeoutException e) {
                throw new RuntimeException(e);
            }
        }).whenCompleteAsync((result, throwable) -> deferredResult.setResult(result));
        return deferredResult;
    }

    /**
     Sample usage: curl "http://localhost:8080/ioService/setLogicalLevelDigitalOutput?&output=4&level=1"
     */
    @ApiOperation(value = "Set logical level to digital output. Controller command A")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Logical 'level' set to digital 'output'") })
    @RequestMapping(value = "/setLogicalLevelDigitalOutput", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public DeferredResult<ResultWithSecondInputParameter> setLogicalLevelDigitalOutput(
            @ApiParam(value = "digital output 0-31", required = true) @RequestParam(value = "digitalOutput", required = true) int digitalOutput,
            @ApiParam(value = "logical level 0 or 1", required = true) @RequestParam(value = "logicalLevel", required = true) int logicalLevel) {
        DeferredResult<ResultWithSecondInputParameter> deferredResult = new DeferredResult<>();
        CompletableFuture.supplyAsync(() -> {
            try {
                return ioServiceImpl.setLogicalLevelDigitalOutput(digitalOutput, logicalLevel);
            } catch (SerialPortException e) {
                throw new RuntimeException(e);
            }
        }).whenCompleteAsync((result, throwable) -> deferredResult.setResult(result));
        return deferredResult;
    }

    /**
     Sample usage: curl "http://localhost:8080/ioService/getLogicalLevelDigitalOutput?&output=4"
     */
    @ApiOperation(value = "Get logical level on digital output. Controller command a")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Logical level on digital 'output' is 0 or 1") })
    @RequestMapping(value = "/getLogicalLevelDigitalOutput", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public DeferredResult<ResultWithOutputValue> getLogicalLevelDigitalOutput(
            @ApiParam(value = "digital output 0-31", required = true) @RequestParam(value = "digitalOutput", required = true) int digitalOutput) {
        DeferredResult<ResultWithOutputValue> deferredResult = new DeferredResult<>();
        CompletableFuture.supplyAsync(() -> {
            try {
                return ioServiceImpl.getLogicalLevelDigitalOutput(digitalOutput);
            } catch (SerialPortException | SerialPortTimeoutException e) {
                throw new RuntimeException(e);
            }
        }).whenCompleteAsync((result, throwable) -> deferredResult.setResult(result));
        return deferredResult;
    }

    /**
     Sample usage: curl "http://localhost:8080/ioService/setByteDigitalOutputBlock?&block=0&byteValue=128"
     */
    @ApiOperation(value = "Set byte to digital output block. Controller command B")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "'byte' set on digital output 'block'") })
    @RequestMapping(value = "/setByteDigitalOutputBlock", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public DeferredResult<ResultWithSecondInputParameter> setByteDigitalOutputBlock(
            @ApiParam(value = "digital output block 0-3", required = true) @RequestParam(value = "digitalOutputBlock", required = true) int digitalOutputBlock,
            @ApiParam(value = "byte value 0-255", required = true) @RequestParam(value = "byteValue", required = true) int byteValue) {
        DeferredResult<ResultWithSecondInputParameter> deferredResult = new DeferredResult<>();
        CompletableFuture.supplyAsync(() -> {
            try {
                return ioServiceImpl.setByteDigitalOutputBlock(digitalOutputBlock, byteValue);
            } catch (SerialPortException e) {
                throw new RuntimeException(e);
            }
        }).whenCompleteAsync((result, throwable) -> deferredResult.setResult(result));
        return deferredResult;
    }

    /**
     Sample usage: curl "http://localhost:8080/ioService/getByteDigitalOutputBlock?&block=0"
     */
    @ApiOperation(value = "Get byte on digital output block (8 outputs). Controller command b")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "byte value 0-255") })
    @RequestMapping(value = "/getByteDigitalOutputBlock", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public DeferredResult<ResultWithOutputValue> getByteDigitalOutputBlock(
            @ApiParam(value = "digital output block 0-3", required = true) @RequestParam(value = "digitalOutputBlock", required = true) int digitalOutputBlock) {
        DeferredResult<ResultWithOutputValue> deferredResult = new DeferredResult<>();
        CompletableFuture.supplyAsync(() -> {
            try {
                return ioServiceImpl.getByteDigitalOutputBlock(digitalOutputBlock);
            } catch (SerialPortException | SerialPortTimeoutException e) {
                throw new RuntimeException(e);
            }
        }).whenCompleteAsync((result, throwable) -> deferredResult.setResult(result));
        return deferredResult;
    }

    /**
     Sample usage: curl "http://localhost:8080/ioService/getByteDigitalInputBlock?&block=0"
     */
    @ApiOperation(value = "Get byte on digital input block (8 inputs). Controller command f")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "byte value 0-255") })
    @RequestMapping(value = "/getByteDigitalInputBlock", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public DeferredResult<ResultWithOutputValue> getByteDigitalInputBlock(
            @ApiParam(value = "digital input block 0-3", required = true) @RequestParam(value = "digitalInputBlock", required = true) int digitalInputBlock) {
        DeferredResult<ResultWithOutputValue> deferredResult = new DeferredResult<>();
        CompletableFuture.supplyAsync(() -> {
            try {
                return ioServiceImpl.getByteDigitalInputBlock(digitalInputBlock);
            } catch (SerialPortException | SerialPortTimeoutException e) {
                throw new RuntimeException(e);
            }
        }).whenCompleteAsync((result, throwable) -> deferredResult.setResult(result));
        return deferredResult;
    }

    /**
     Sample usage: curl "http://localhost:8080/ioService/getLogicalLevelDigitalInput?&input=0"
     */
    @ApiOperation(value = "Get logical level on digital input. Controller command e")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "logical level 0 or 1") })
    @RequestMapping(value = "/getLogicalLevelDigitalInput", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public DeferredResult<ResultWithOutputValue> getLogicalLevelDigitalInput(
            @ApiParam(value = "digital input 0-31", required = true) @RequestParam(value = "digitalInput", required = true) int digitalInput) {
        DeferredResult<ResultWithOutputValue> deferredResult = new DeferredResult<>();
        CompletableFuture.supplyAsync(() -> {
            try {
                return ioServiceImpl.getLogicalLevelDigitalInput(digitalInput);
            } catch (SerialPortException | SerialPortTimeoutException e) {
                throw new RuntimeException(e);
            }
        }).whenCompleteAsync((result, throwable) -> deferredResult.setResult(result));
        return deferredResult;
    }

    /**
     Sample usage: curl "http://localhost:8080/ioService/setVoltageAnalogOutput?&output=0&voltage=124"
     */
    @ApiOperation(value = "Set voltage on analog output. Controller command U")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Set 'voltage' V on analog output 'output'") })
    @RequestMapping(value = "/setVoltageAnalogOutput", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public DeferredResult<ResultWithSecondInputParameter> setVoltageAnalogOutput(
            @ApiParam(value = "analog output 0-3", required = true) @RequestParam(value = "analogOutput", required = true) int analogOutput,
            @ApiParam(value = "voltage/100 in V 0-1023. 1023 equals 10.23 V", required = true) @RequestParam(value = "voltage", required = true) int voltage) {
        DeferredResult<ResultWithSecondInputParameter> deferredResult = new DeferredResult<>();
        CompletableFuture.supplyAsync(() -> {
            try {
                return ioServiceImpl.setVoltageAnalogOutput(analogOutput, voltage);
            } catch (SerialPortException e) {
                throw new RuntimeException(e);
            }
        }).whenCompleteAsync((result, throwable) -> deferredResult.setResult(result));
        return deferredResult;
    }

    /**
     Sample usage: curl "http://localhost:8080/ioService/getVoltageAnalogOutput?&output=0"
     */
    @ApiOperation(value = "Get voltage on analog output. Controller command u")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "voltage in V 0-10.23") })
    @RequestMapping(value = "/getVoltageAnalogOutput", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public DeferredResult<ResultWithOutputValue> getVoltageAnalogOutput(
            @ApiParam(value = "analog output 0-3", required = true) @RequestParam(value = "analogOutput", required = true) int analogOutput) {
        DeferredResult<ResultWithOutputValue> deferredResult = new DeferredResult<>();
        CompletableFuture.supplyAsync(() -> {
            try {
                return ioServiceImpl.getVoltageAnalogOutput(analogOutput);
            } catch (SerialPortException | SerialPortTimeoutException e) {
                throw new RuntimeException(e);
            }
        }).whenCompleteAsync((result, throwable) -> deferredResult.setResult(result));
        return deferredResult;
    }

    /**
     Sample usage: curl "http://localhost:8080/ioService/getVoltageAnalogInput?&input=0"
     */
    @ApiOperation(value = "Get voltage on analog input. Controller command v")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "voltage in V 0-10.23") })
    @RequestMapping(value = "/getVoltageAnalogInput", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public DeferredResult<ResultWithOutputValue> getVoltageAnalogInput(
            @ApiParam(value = "analog input 0-3", required = true) @RequestParam(value = "analogInput", required = true) int analogInput) {
        DeferredResult<ResultWithOutputValue> deferredResult = new DeferredResult<>();
        CompletableFuture.supplyAsync(() -> {
            try {
                return ioServiceImpl.getVoltageAnalogInput(analogInput);
            } catch (SerialPortException | SerialPortTimeoutException e) {
                throw new RuntimeException(e);
            }
        }).whenCompleteAsync((result, throwable) -> deferredResult.setResult(result));
        return deferredResult;
    }
}