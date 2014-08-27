package io.github.avatarhurden.tribalwarsengine.main;

import com.littlebigberry.httpfiledownloader.FileDownloader;
import com.littlebigberry.httpfiledownloader.FileDownloaderDelegate;
import io.github.avatarhurden.tribalwarsengine.frames.MainWindow;

import javax.swing.*;

/**
 * Responsavel por verificar se o usuario deseja atualizar o app
 * e baixar a versão mais recente
 *
 * @author Wesley Nascimento.
 */
public class Updater implements FileDownloaderDelegate {

    private String url;
    private double version;

    /**
     * Inicia o objeto, com as informações recebidas do servidor.
     *
     * @param version - Versão remota do App
     * @param url     - Url para download
     */
    public Updater(double version, String url) {
        this.url = url;
        this.version = version;
    }

    /**
     * Pergunta se o usuario deseja atualizar ou não o app
     */
    public void start() {

        if (!Configuration.get().getConfig("show_new_updates", true)) {
            return;
        }

        Object[] options = {"Atualizar agora!", "Atualizar mais tarde!"};

        JCheckBox check = new JCheckBox("Não me perguntar novamente");
        String mensagem = "Existe uma versão mais recente do Tribal Wars Engine!\n"
                + "Deseja atualizar da versão " + Main.VERSION + " para a versão " + this.version + "?";

        int n = JOptionPane.showOptionDialog(MainWindow.getInstance(), new Object[]{mensagem, check},
                "Nova atualização!", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

        if (check.isSelected()) {
            Configuration.get().setConfig("show_new_updates", false);
        }

        if (n == 0) {
            beginDownload();
        }
    }


    /**
     * Inicia o download usando a url remota e baixando no local no jar sendo executado.
     */
    private void beginDownload() {
        FileDownloader fileDownloader = new FileDownloader(this);
        fileDownloader.setUrl(url);
        fileDownloader.setLocalLocation(Main.class.getProtectionDomain().getCodeSource().getLocation().getPath());
        fileDownloader.beginDownload();
    }

    @Override
    public void didStartDownload(FileDownloader fileDownloader) {
        JOptionPane.showMessageDialog(MainWindow.getInstance(), "O download da versão " + this.version + " foi iniciado e está sendo executado em segundo plano.", "Download iniciado!", JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public void didProgressDownload(FileDownloader fileDownloader) {

    }

    @Override
    public void didFinishDownload(FileDownloader fileDownloader) {
        JOptionPane.showMessageDialog(MainWindow.getInstance(), "O download foi concluido com sucesso. Por favor, reinicie o Tribal Wars Engine!", "Download concluido!", JOptionPane.INFORMATION_MESSAGE);
        System.exit(-1);
    }

    @Override
    public void didFailDownload(FileDownloader fileDownloader) {
        JOptionPane.showMessageDialog(MainWindow.getInstance(), "O download não pode ser concluido, pois houve um problema durante o processo.", "Download falhou!", JOptionPane.ERROR_MESSAGE);
    }
}
