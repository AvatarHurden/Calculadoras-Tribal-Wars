package main;

import com.littlebigberry.httpfiledownloader.FileDownloader;
import com.littlebigberry.httpfiledownloader.FileDownloaderDelegate;
import frames.MainWindow;

import javax.swing.*;

/*
 *
 * Created by Administrador on 07/08/2014.
 */
public class Updater implements FileDownloaderDelegate {

    private String url;
    private double version;

    public Updater( double version, String url ){
        this.url = url;
        this.version = version;
    }

    public void start(){
        Object[] options = {"Atualizar agora!", "Atualizar mais tarde!"};

        JCheckBox check = new JCheckBox("N�o me perguntar novamente");
        String mensagem = "Existe uma vers�o mais recente do Tribal Wars Engine!\n"
                + "Deseja atualizar da vers�o " + Main.VERSION + " para a vers�o " + this.version + "?";

        int n = JOptionPane.showOptionDialog(MainWindow.getInstance() , new Object[]{mensagem, check},
                "Nova atualiza��o!", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

        if (check.isSelected()) {
            //Aguardando sistema de configura��es baseado em json!
        }

        if (n == 0) {
            beginDownload();
        }
    }


    public void beginDownload(){
        FileDownloader fileDownloader = new FileDownloader( this );
        fileDownloader.setUrl( url );
        fileDownloader.setLocalLocation( Main.class.getProtectionDomain().getCodeSource().getLocation().getPath() );
        fileDownloader.beginDownload();
    }

    @Override
    public void didStartDownload(FileDownloader fileDownloader) {
        JOptionPane.showMessageDialog(MainWindow.getInstance(), "O download da vers�o " + this.version + " foi iniciado e est� sendo executado em segundo plano.", "Download iniciado!", JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public void didProgressDownload(FileDownloader fileDownloader) {

    }

    @Override
    public void didFinishDownload(FileDownloader fileDownloader) {
        JOptionPane.showMessageDialog(MainWindow.getInstance(), "O download foi concluido com sucesso. Por favor,reinicie o Tribal Wars Engine!", "Download concluido!", JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public void didFailDownload(FileDownloader fileDownloader) {
        JOptionPane.showMessageDialog(MainWindow.getInstance(), "O download n�o pode ser concluido, pois houve um problema durante o processo.", "Download falhou!", JOptionPane.ERROR_MESSAGE);
    }
}
