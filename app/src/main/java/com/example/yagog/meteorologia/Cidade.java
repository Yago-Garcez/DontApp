package com.example.yagog.meteorologia;

import com.google.firebase.database.Exclude;

public class Cidade {
    public String chave, nome;

    public Cidade(String nome) {
        setNome(nome);
    }

    public Cidade(String chave, String nome) {
        this (nome);
        setChave(chave);
    }

    public Cidade() {
    }

    @Exclude
    public String getChave() {
        return chave;
    }

    public void setChave(String chave) {
        this.chave = chave;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public String toString() {
        return "Cidade{" +
                "chave='" + chave + '\'' +
                ", nome='" + nome + '\'' +
                '}';
    }
}