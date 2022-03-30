package Yang.Stream;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.Builder;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;

import static Hardware.Computer.*;

public class Header {//负责数据流header内容的转化
    private StreamId streamId;
    private StreamRank streamRank;
    private EndStationInterface endStationInterface;
    private DateFrameSpecification dateFrameSpecification;
    private TrafficSpecification trafficSpecification;
    private UserToNetworkRequirements userToNetworkRequirements;
    private InterfaceCapabilities interfaceCapabilities;
    private ConfigResult configResult;

    @Builder
    public Header(@NonNull String uniqueId, @NonNull short rank){
        this.streamId = new StreamId(uniqueId);
        this.streamRank = new StreamRank(rank);
        this.endStationInterface = new EndStationInterface();
        this.dateFrameSpecification = new DateFrameSpecification();
        this.trafficSpecification = new TrafficSpecification();
        this.userToNetworkRequirements = new UserToNetworkRequirements();
        this.interfaceCapabilities = new InterfaceCapabilities();
        this.configResult = new ConfigResult();
    }

    public String getKey(){
        return this.streamId.getJSONObject();
    }

    private class StreamId{
        String uniqueID;

        public StreamId(String uniqueID){
            this.uniqueID = uniqueID;
            System.out.println("Generate StreamID - UniqueID " + this.uniqueID);
        }

        String getJSONObject(){
//            JSONObject object = new JSONObject();
            String stream_id_type = device_mac.get(0) + ":" + uniqueID;
//            object.put("stream-id", stream_id_type);
//            return object;
            return stream_id_type;
        }
    }//finished

    private class StreamRank {//rank
        short rank;
        /*
         * 0 流量紧急业务
         * 1 非紧急业务
         * */
        public StreamRank(short rank){
            this.rank = rank;
        }

        JSONObject getJSONObject(){
            JSONObject object = new JSONObject();
            object.put("rank", rank);
            return object;
        }
    }//finished

    private class EndStationInterface {
        String macAddress, interfaceName;

        public EndStationInterface(){
            this.macAddress = device_mac.get(0);
            this.interfaceName = host_name;
        }

        JSONObject getJSONObject(){
            JSONObject object = new JSONObject();
            object.put("mac-address", macAddress);
            object.put("interface-name", interfaceName);
            return object;
        }
    }//finished

    private class DateFrameSpecification {
        String destinationMacAddress, sourceMacAddress;
        short priorityCodePoint, vlanId;
        int index;
        /*
         * PriorityCodePoint：VLAN Tag的PCP(PriorityCodePoint)字段，取值范围为0~7，用于标识网桥中的流类
         * VlanId：标识VLAN Tag的VLAN ID字段，取值范围为0~4095，如果仅知道PriorityCodePoint，VlanId则指定为0
         * */

        //IPvX
        String sourceIpAddressV4, destinationIpAddressV4, sourceIpAddressV6, destinationIpAddressV6;
        short dscpV4, dscpV6;
        int protocolV4, sourcePortV4, destinationPortV4, protocolV6, sourcePortV6, destinationPortV6;

        public DateFrameSpecification(){
            this.index = 0;

            this.destinationMacAddress = "00-00-00-00-00-00";
            this.sourceMacAddress = device_mac.get(0);
            this.priorityCodePoint = 0;
            this.vlanId = 0;

            this.sourceIpAddressV4 = device_ip;
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

        JSONArray getJSONObject(){
            JSONArray jsonArray = new JSONArray();

            JSONObject dateFrameSpecification1 = new JSONObject();
            dateFrameSpecification1.put("index", this.index);
            this.index += 1;
            JSONObject object1 = new JSONObject();
            object1.put("destination-mac-address", destinationMacAddress);
            object1.put("source-mac-address", sourceMacAddress);
            dateFrameSpecification1.put("ieee802-mac-addresses", object1);
            jsonArray.add(dateFrameSpecification1);

            JSONObject dateFrameSpecification2 = new JSONObject();
            JSONObject object2 = new JSONObject();
            dateFrameSpecification2.put("index", this.index);
            this.index += 1;
            object2.put("priority-code-point", priorityCodePoint);
            object2.put("vlan-id", vlanId);
            dateFrameSpecification2.put("ieee802-vlan-tag", object2);
            jsonArray.add(dateFrameSpecification2);

            //IPv4
            JSONObject dateFrameSpecification3 = new JSONObject();
            JSONObject object3 = new JSONObject();
            dateFrameSpecification3.put("index", this.index);
            this.index += 1;
            object3.put("source-ip-address", sourceIpAddressV4);
            object3.put("destination-ip-address", destinationIpAddressV4);
            object3.put("dscp", dscpV4);
            object3.put("protocol", protocolV4);
            object3.put("source-port", sourcePortV4);
            object3.put("destination-port", destinationPortV4);
            dateFrameSpecification3.put("ipv4-tuple", object3);
            jsonArray.add(dateFrameSpecification3);
            //IPv6
            JSONObject dateFrameSpecification4 = new JSONObject();
            JSONObject object4 = new JSONObject();
            dateFrameSpecification4.put("index", this.index);
            this.index += 1;
            object4.put("source-ip-address", sourceIpAddressV6);
            object4.put("destination-ip-address", destinationIpAddressV6);
            object4.put("dscp", dscpV6);
            object4.put("protocol", protocolV6);
            object4.put("source-port", sourcePortV6);
            object4.put("destination-port", destinationPortV6);
            dateFrameSpecification4.put("ipv6-tuple", object4);
            jsonArray.add(dateFrameSpecification4);

            return jsonArray;
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

            JSONObject object1 = new JSONObject();
            object1.put("numerator", this.numerator);
            object1.put("denominator", this.denominator);
            object.put("interval", object1);
            object.put("max-frames-per-interval", maxFramesPerInterval);
            object.put("max-frame-size", maxFrameSize);
            object.put("transmission-selection", transmissionSelection);
            JSONObject object2 = new JSONObject();
            object2.put("earliest-transmit-offset", earliestTransmitOffset);
            object2.put("latest-transmit-offset", latestTransmitOffset);
            object2.put("jitter", jitter);
            object.put("time-aware", object2);
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

    private class ConfigResult{
        public boolean isConfig;

        public ConfigResult(){
            this.isConfig = false;
        }

        JSONObject getJSONObject(){
            JSONObject object = new JSONObject();
            object.put("is-config", this.isConfig);
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
        if(isEndStationInterface) streamHeader.put("end-station-interfaces",
                this.endStationInterface.getJSONObject());
        if(isDateFrameSpecification) streamHeader.put("data-frame-specification",
                this.dateFrameSpecification.getJSONObject());
        if(isTrafficSpecification) streamHeader.put("traffic-specification",
                this.trafficSpecification.getJSONObject());
        if(isUserToNetworkRequirements) streamHeader.put("user-to-network-requirements",
                this.userToNetworkRequirements.getJSONObject());
        if(isInterfaceCapabilities) streamHeader.put("interface-capabilities",
                this.interfaceCapabilities.getJSONObject());
        streamHeader.put("config-result", this.configResult.getJSONObject());
        return streamHeader;
    }
}
