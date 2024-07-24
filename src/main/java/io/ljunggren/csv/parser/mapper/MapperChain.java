package io.ljunggren.csv.parser.mapper;

import java.lang.reflect.Field;

public abstract class MapperChain {

    protected MapperChain nextChain;
    
    public MapperChain nextChain(MapperChain nextChain) {
        this.nextChain = nextChain;
        return this;
    }
    
    public abstract Object map(Field field, String value) throws Exception;
    
}
