package jugistanbul.helper;

import jugistanbul.entity.Speaker;

/**
 * @author hakdogan (hakdogan@kodcu.com)
 * Created on 13.02.2020
 **/

public class PersistObject {

    private Speaker speaker;
    private Operation operation;

    public PersistObject(Speaker speaker, Operation operation) {
        this.speaker = speaker;
        this.operation = operation;
    }

    public Speaker getSpeaker() {
        return speaker;
    }

    public void setSpeaker(Speaker speaker) {
        this.speaker = speaker;
    }

    public Operation getOperation() {
        return operation;
    }

    public void setOperation(Operation operation) {
        this.operation = operation;
    }
}
