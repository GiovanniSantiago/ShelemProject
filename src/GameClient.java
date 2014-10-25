import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;


public class GameClient {
	public static ClientNetworkHandler handler = null;
	public static JFrame f=null;
	public static void main(String[] args) throws IOException {
		System.out.println("CLIENT STARTED!");
		System.out.println("RUNNING NETWORK HANDLER THREAD");
		handler = new ClientNetworkHandler();
		new Thread(handler).start();
		System.out.println("MAIN CLIENT DONE");
		f = new JFrame("SDSADSA");
		JButton b = new JButton("SADKSJAHDJKSADHJKLASKLSADJL");
		b.setPreferredSize(new Dimension(640,480));
		b.addMouseListener(new MouseListener() {


			@Override
			public void mouseClicked(MouseEvent e) {
				handler.line.sendMessage(new Message(new MessagePair(MessageConstants.PARAM_NAME,MessageConstants.CLIENT_UPDATE_QUITGAME)));
				/*
				 * Do something to close window or something here
				 */
				f.dispatchEvent(new WindowEvent(f,WindowEvent.WINDOW_CLOSING));
			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
		});
		f.add(b);
		f.pack();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setVisible(true);
	}
}
