package CSCoder;

import lombok.Getter;
import lombok.Setter;

/**
 * @author : wpy
 * @description: TODO
 * @date : 3/15/22 6:35 PM
 */
public class RpcRequest {
    @Getter
    @Setter
    private String id;

    @Getter
    @Setter
    private Object data;

    @Override
    public String toString(){
        return "RpcRequest{id=\'" + id + "\', data=" + data + "}";
    }
}
