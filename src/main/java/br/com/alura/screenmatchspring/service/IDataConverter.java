package br.com.alura.screenmatchspring.service;

public interface IDataConverter {
    <T> T getData(String json, Class<T> className);
}
