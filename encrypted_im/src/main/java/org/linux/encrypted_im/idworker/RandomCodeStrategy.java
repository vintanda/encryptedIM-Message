package org.linux.encrypted_im.idworker;

public interface RandomCodeStrategy {
    void init();

    int prefix();

    int next();

    void release();
}
