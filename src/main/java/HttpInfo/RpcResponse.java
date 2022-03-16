package HttpInfo;

import lombok.Getter;
import lombok.Setter;

/**
 * @author : wpy
 * @description: TODO
 * @date : 3/15/22 6:35 PM
 */
public class RpcResponse {
    @Getter
    @Setter
    private String id;

    @Getter
    @Setter
    private Object data;

    @Getter
    @Setter
    private int status;

    @Override
    public String toString(){
        return "RpcResponse{id=\'" + id + "\', data=" + data + ", status=" + status + "}";
    }
}
