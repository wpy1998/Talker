package Yang.Stream;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.Builder;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;

import static Hardware.Computer.*;

public class Header {//负责数据流header内容的转化
    //streamId
    private String uniqueId;

    //streamRank
    private short rank;

    //endStationInterface
    private String macAddress, interfaceName;

    //dateFrameSpecification
    private int index, protocolV4, sourcePortV4, destinationPortV4, protocolV6, sourcePortV6,
            destinationPortV6;
    private String destinationMacAddress, sourceMacAddress, sourceIpAddressV4,
            destinationIpAddressV4, sourceIpAddressV6, destinationIpAddressV6;
    private short priorityCodePoint, vlanId, dscpV4, dscpV6;

    //trafficSpecification
    private int numerator, denominator, earliestTransmitOffset, latestTransmitOffset, jitter;
    private short maxFramesPerInterval, maxFrameSize, transmissionSelection;

    //userToNetworkRequirements
    private short numSeamlessTrees;
    private int maxLatency;

    //interfaceCapabilities
    private boolean vlanTagCapable;
    private List<Integer> cBStreamIdenTypeList, cBSequenceTypeList;

    //configResult
    public boolean isConfig;

    @Builder
    public Header(@NonNull String uniqueId, @NonNull short rank,
                  String dest_mac, String dest_ip){
        //streamId
        this.uniqueId = uniqueId;
        System.out.println("Generate StreamID - UniqueID " + this.uniqueId);

        //streamRank
        this.rank = rank;

        //endStationInterface
        this.macAddress = macs.get(0);
        this.interfaceName = host_merge;

        //dateFrameSpecification
        this.index = 0;

        this.destinationMacAddress = (dest_mac == null) ? macs.get(0) : dest_mac;
        this.sourceMacAddress = macs.get(0);
        this.priorityCodePoint = 0;
        this.vlanId = 0;

        this.sourceIpAddressV4 = ipv4s.get(0);
        this.destinationIpAddressV4 = (dest_ip == null) ? "127.0.0.1" : dest_ip;
        this.dscpV4 = 0;
        this.protocolV4 = 0;
        this.sourcePortV4 = 0;
        this.destinationPortV4 = 0;

        this.sourceIpAddressV6 = ipv6s.get(0);
        this.destinationIpAddressV6 = "0000:0000:0000:0000:0000:0000:0000:0000";
        this.dscpV6 = 0;
        this.protocolV6 = 0;
        this.sourcePortV6 = 0;
        this.destinationPortV6 = 0;

        //trafficSpecification
        this.numerator = 0;
        this.denominator = 1000000000;

        this.maxFrameSize = 0;
        this.maxFramesPerInterval = 0;
        this.transmissionSelection = 0;

        this.earliestTransmitOffset = 0;
        this.latestTransmitOffset = 0;
        this.jitter = 0;

        //userToNetworkRequirements
        this.numSeamlessTrees = 0;
        this.maxLatency = 0;

        //interfaceCapabilities
        this.vlanTagCapable = false;
        this.cBStreamIdenTypeList = new ArrayList<>();
        this.cBSequenceTypeList = new ArrayList<>();

        //configResult
        this.isConfig = false;
    }

    public String getKey(){
        return getStreamIdJSONObject();
    }

    public JSONObject getJSONObject(boolean isStreamID, boolean isStreamRank,
                                    boolean isEndStationInterface,
                                    boolean isDateFrameSpecification,
                                    boolean isTrafficSpecification,
                                    boolean isUserToNetworkRequirements,
                                    boolean isInterfaceCapabilities){
        JSONObject streamHeader = new JSONObject();
        if(isStreamID) streamHeader.put("stream-id", getStreamIdJSONObject());
        if(isStreamRank) streamHeader.put("stream-rank", getStreamRankJSONObject());
        if(isEndStationInterface) streamHeader.put("end-station-interfaces",
                getEndStationInterfaceJSONObject());
        if(isDateFrameSpecification) streamHeader.put("data-frame-specification",
                getDateFrameSpecificationJSONObject());
        if(isTrafficSpecification) streamHeader.put("traffic-specification",
                getTrafficSpecificationJSONObject());
        if(isUserToNetworkRequirements) streamHeader.put("user-to-network-requirements",
                getUserToNetworkRequirementsJSONObject());
        if(isInterfaceCapabilities) streamHeader.put("interface-capabilities",
                getInterfaceCapabilitiesJSONObject());
        streamHeader.put("config-result", getConfigResultJSONObject());
        return streamHeader;
    }

    String getStreamIdJSONObject(){
        String stream_id_type = this.macAddress.replace(":", "-") +
                ":" + uniqueId;
        return stream_id_type;
    }

    JSONObject getStreamRankJSONObject(){
        JSONObject object = new JSONObject();
        object.put("rank", rank);
        return object;
    }

    JSONObject getEndStationInterfaceJSONObject(){
        JSONObject object = new JSONObject();
        object.put("mac-address", macAddress);
        object.put("interface-name", interfaceName);
        return object;
    }

    JSONArray getDateFrameSpecificationJSONObject(){
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

    JSONObject getTrafficSpecificationJSONObject(){
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

    JSONObject getUserToNetworkRequirementsJSONObject(){
        JSONObject object = new JSONObject();
        object.put("num-seamless-trees", numSeamlessTrees);
        object.put("max-latency", maxLatency);
        return object;
    }

    JSONObject getInterfaceCapabilitiesJSONObject(){
        JSONObject object = new JSONObject();
        object.put("vlan-tag-capable", vlanTagCapable);
        object.put("cb-stream-iden-type-list", cBStreamIdenTypeList);
        object.put("cb-sequence-type-list", cBSequenceTypeList);
        return object;
    }

    JSONObject getConfigResultJSONObject(){
        JSONObject object = new JSONObject();
        object.put("is-config", this.isConfig);
        return object;
    }
}
