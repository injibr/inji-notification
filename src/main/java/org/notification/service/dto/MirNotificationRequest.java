package org.notification.service.dto;

import java.util.List;
import java.util.Map;

public class MirNotificationRequest {
    private String titulo;
    private String corpoDaMensagem;
    private List<String> cpfs;
    private String tipoFonte;
    private String acao;
    private Map<String, Object> dados;
    private boolean flagGeraNotificacao;

    public MirNotificationRequest(String titulo, String corpoDaMensagem, List<String> cpfs,
                                   String tipoFonte, String acao, Map<String, Object> dados,
                                   boolean flagGeraNotificacao) {
        this.titulo = titulo;
        this.corpoDaMensagem = corpoDaMensagem;
        this.cpfs = cpfs;
        this.tipoFonte = tipoFonte;
        this.acao = acao;
        this.dados = dados;
        this.flagGeraNotificacao = flagGeraNotificacao;
    }

    public String getTitulo() { return titulo; }
    public String getCorpoDaMensagem() { return corpoDaMensagem; }
    public List<String> getCpfs() { return cpfs; }
    public String getTipoFonte() { return tipoFonte; }
    public String getAcao() { return acao; }
    public Map<String, Object> getDados() { return dados; }
    public boolean isFlagGeraNotificacao() { return flagGeraNotificacao; }
}
