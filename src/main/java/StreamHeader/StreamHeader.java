package StreamHeader;

import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static Hardware.HardwareMessage.device_mac;

public class StreamHeader {//负责数据流header内容的转化
    private StreamId streamId;
    private StreamRank streamRank;
    private EndStationInterface endStationInterface;
    private DateFrameSpecification dateFrameSpecification;
    private TrafficSpecification trafficSpecification;
    private UserToNetworkRequirements userToNetworkRequirements;
    private InterfaceCapabilities interfaceCapabilities;

    public StreamHeader(){
        this.streamId = new StreamId();
        this.streamRank = new StreamRank();
        this.endStationInterface = new EndStationInterface();
        this.dateFrameSpecification = new DateFrameSpecification();
        this.trafficSpecification = new TrafficSpecification();
        this.userToNetworkRequirements = new UserToNetworkRequirements();
        this.interfaceCapabilities = new InterfaceCapabilities();
    }

    private class StreamId{//macAddress, uniqueID
        String uniqueID;

        public StreamId(){
            this.uniqueID = "00-00";
        }

        JSONObject getJSONObject(){
            JSONObject object = new JSONObject();
            object.put("mac-address", device_mac);
            object.put("unique-id", uniqueID);
            return object;
        }
    }

    private class StreamRank {//rank
        short rank;
        /*
         * 0 流量紧急业务
         * 1 非紧急业务
         * */
        public StreamRank(){
            this.rank = 1;
        }

        JSONObject getJSONObject(){
            JSONObject object = new JSONObject();
            object.put("rank", rank);
            return object;
        }
    }

    private class EndStationInterface {
        String macAddress, interfaceName;

        public EndStationInterface(){
            this.macAddress = "00-00-00-00-00-00";
            this.interfaceName = "default-name";
        }

        JSONObject getJSONObject(){
            JSONObject object = new JSONObject();
            object.put("mac-address", macAddress);
            object.put("interface-name", interfaceName);
            return object;
        }
    }

    private class DateFrameSpecification {
        String destinationMacAddress, sourceMacAddress;
        short priorityCodePoint, vlanId;
        /*
         * PriorityCodePoint：VLAN Tag的PCP(PriorityCodePoint)字段，取值范围为0~7，用于标识网桥中的流类
         * VlanId：标识VLAN Tag的VLAN ID字段，取值范围为0~4095，如果仅知道PriorityCodePoint，VlanId则指定为0
         * */

        //IPvX
        String sourceIpAddressV4, destinationIpAddressV4, sourceIpAddressV6, destinationIpAddressV6;
        short dscpV4, dscpV6;
        int protocolV4, sourcePortV4, destinationPortV4, protocolV6, sourcePortV6, destinationPortV6;

        public DateFrameSpecification(){
            this.destinationMacAddress = "00-00-00-00-00-00";
            this.sourceMacAddress = "00-00-00-00-00-00";
            this.priorityCodePoint = 0;
            this.vlanId = 0;

            this.sourceIpAddressV4 = "0.0.0.0";
            this.destinationIpAddressV4 = "0.0.0.0";
            this.dscpV4 = 0;
            this.protocolV4 = 0;
            this.sourcePortV4 = 0;
            this.destinationPortV4 = 0;

            this.sourceIpAddressV6 = "0000:0000:0000:0000:0000:0000:0000:0000";
            this.destinationIpAddressV6 = "0000:0000:0000:0000:0000:0000:0000:0000";
            this.dscpV6 = 0;
            this.protocolV6 = 0;
            this.sourcePortV6 = 0;
            this.destinationPortV6 = 0;
        }

        JSONObject getJSONObject(){
            JSONObject object = new JSONObject();
            object.put("destination-mac-address", destinationMacAddress);
            object.put("source-mac-address", sourceMacAddress);
            object.put("priority-code-point", priorityCodePoint);
            object.put("vlan-id", vlanId);
            //IPv4
            object.put("source-ip-address-v4", sourceIpAddressV4);
            object.put("destination-ip-address-v4", destinationIpAddressV4);
            object.put("dscp-v4", dscpV4);
            object.put("protocol-v4", protocolV4);
            object.put("source-port-v4", sourcePortV4);
            object.put("destination-port-v4", destinationPortV4);
            //IPv6
            object.put("source-ip-address-v6", sourceIpAddressV6);
            object.put("destination-ip-address-v6", destinationIpAddressV6);
            object.put("dscp-v6", dscpV6);
            object.put("protocol-v6", protocolV6);
            object.put("source-port-v6", sourcePortV6);
            object.put("destination-port-v6", destinationPortV6);
            return object;
        }
    }

    private class TrafficSpecification {
        int numerator, denominator;
        short maxFramesPerInterval, maxFrameSize, transmissionSelection;

        int earliestTransmitOffset, latestTransmitOffset, jitter;

        public TrafficSpecification(){
            this.numerator = 0;
            this.denominator = 1000000000;

            this.maxFrameSize = 0;
            this.maxFramesPerInterval = 0;
            this.transmissionSelection = 0;

            this.earliestTransmitOffset = 0;
            this.latestTransmitOffset = 0;
            this.jitter = 0;
        }

        JSONObject getJSONObject(){
            JSONObject object = new JSONObject();
            object.put("numerator", this.numerator);
            object.put("denominator", this.denominator);
            object.put("max-frames-per-interval", maxFramesPerInterval);
            object.put("max-frame-size", maxFrameSize);
            object.put("transmission-selection", transmissionSelection);
            object.put("earliest-transmit-offset", earliestTransmitOffset);
            object.put("latest-transmit-offset", latestTransmitOffset);
            object.put("jitter", jitter);
            return object;
        }
    }

    private class UserToNetworkRequirements {
        short numSeamlessTrees;
        int maxLatency;

        public UserToNetworkRequirements(){
            this.numSeamlessTrees = 0;
            this.maxLatency = 0;
        }

        JSONObject getJSONObject(){
            JSONObject object = new JSONObject();
            object.put("num-seamless-trees", numSeamlessTrees);
            object.put("max-latency", maxLatency);
            return object;
        }
    }

    private class InterfaceCapabilities {
        boolean vlanTagCapable;
        List<Integer> cBStreamIdenTypeList, cBSequenceTypeList;

        public InterfaceCapabilities(){
            this.vlanTagCapable = false;
            this.cBStreamIdenTypeList = new ArrayList<>();
            this.cBSequenceTypeList = new ArrayList<>();
        }

        JSONObject getJSONObject(){
            JSONObject object = new JSONObject();
            object.put("vlan-tag-capable", vlanTagCapable);
            object.put("cb-stream-iden-type-list", cBStreamIdenTypeList);
            object.put("cb-sequence-type-list", cBSequenceTypeList);
            return object;
        }
    }

    public JSONObject getJSONObject(boolean isStreamID, boolean isStreamRank,
                                    boolean isEndStationInterface,
                                    boolean isDateFrameSpecification,
                                    boolean isTrafficSpecification,
                                    boolean isUserToNetworkRequirements,
                                    boolean isInterfaceCapabilities){
        JSONObject streamHeader = new JSONObject();
        if(isStreamID) streamHeader.put("stream-id", this.streamId.getJSONObject());
        if(isStreamRank) streamHeader.put("stream-rank", this.streamRank.getJSONObject());
        if(isEndStationInterface) streamHeader.put("end-station-interface",
                this.endStationInterface.getJSONObject());
        if(isDateFrameSpecification) streamHeader.put("data-frame-specification",
                this.dateFrameSpecification.getJSONObject());
        if(isTrafficSpecification) streamHeader.put("traffic-specification",
                this.trafficSpecification.getJSONObject());
        if(isUserToNetworkRequirements) streamHeader.put("user-to-network-requirements",
                this.userToNetworkRequirements.getJSONObject());
        if(isInterfaceCapabilities) streamHeader.put("interface-capabilities",
                this.interfaceCapabilities.getJSONObject());
        return streamHeader;
    }
}

/*
{
  "input": {
    "header": {
      "interface-capabilities": {
        "cb-sequence-type-list": [
          "0"
        ],
        "vlan-tag-capable": "false",
        "cb-stream-iden-type-list": [
          "0"
        ]
      },
      "date-frame-specification": {
        "source-port-v4": "0",
        "vlan-id": "0",
        "source-port-v6": "0",
        "destination-port-v4": "0",
        "destination-port-v6": "0",
        "protocol-v6": "0",
        "protocol-v4": "0",
        "destination-mac-address": "5c-Da-ce-f7-Ef-DC",
        "destination-ip-address-v6": "C88A:0000:0000:0000:0000:0000:0000:0000",
        "destination-ip-address-v4": "6.59.7.2",
        "source-ip-address-v6": "C88A:0000:0000:0000:0000:0000:0000:0000",
        "priority-code-point": "0",
        "dscp-v4": "0",
        "source-ip-address-v4": "16.60.82.6",
        "dscp-v6": "0",
        "source-mac-address": "6A-Db-2F-ab-eF-Bf"
      },
      "stream-rank": {
        "rank": "0"
      },
      "end-station-interface": {
        "mac-address": "eF-AA-3e-c3-E0-ee",
        "interface-name": "Some interface-name"
      },
      "user-to-network-requirements": {
        "num-seamless-trees": "1",
        "max-latency": "0"
      },
      "traffic-specification": {
        "transmission-selection": "0",
        "max-frames-per-interval": "0",
        "max-frame-size": "0"
      },
      "stream-id": {
        "mac-address": "F9-CD-cF-Ea-87-df",
        "unique-id": "7f-bB"
      }
    }
  }
}
*/