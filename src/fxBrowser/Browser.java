package fxBrowser;

import java.awt.EventQueue;

import javax.swing.JFrame;
import java.awt.BorderLayout;
import javax.swing.JPanel;
import java.awt.FlowLayout;
import javax.swing.JTextField;

import javafx.application.Platform;
import javafx.concurrent.Worker;
import javafx.embed.swing.JFXPanel;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.ResourceBundle;
import java.awt.event.ActionEvent;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JTabbedPane;

public class Browser implements Initializable{

	private JFrame frame;
	private JTextField txtURL;
	WebView wv;
	WebEngine engine;
	JFXPanel fx;
	ArrayList<String>links;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Browser window = new Browser();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Browser() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 1006, 551);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new BorderLayout(0, 0));

		links = new ArrayList<String>();
		
		JPanel panel = new JPanel();
		frame.getContentPane().add(panel, BorderLayout.NORTH);
		panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		JButton btnBack = new JButton("<");
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						if(engine.getHistory().getCurrentIndex()>0){
				            engine.getHistory().go(-1);
				            engine.getLoadWorker().stateProperty().addListener((observable, oldState, newState) -> {
					            if (newState == Worker.State.SUCCEEDED){
					                txtURL.setText(engine.getLocation());
					            }
					        });
				        }
					}
				});
			}
		});
		
		JComboBox comboBox = new JComboBox();
		panel.add(comboBox);
		panel.add(btnBack);
		JButton btnForward = new JButton(">");
		btnForward.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						if (engine.getHistory().getCurrentIndex()+1 < engine.getHistory().getEntries().size()){
				            engine.getHistory().go(1);
				            engine.getLoadWorker().stateProperty().addListener((observable, oldState, newState) -> {
					            if (newState == Worker.State.SUCCEEDED){
					                txtURL.setText(engine.getLocation());
					            }
					        });
				        }
					}
				});
			}
		});
		panel.add(btnForward);

		txtURL = new JTextField();
		panel.add(txtURL);
		txtURL.setColumns(40);
		fx = new JFXPanel();
		frame.getContentPane().add(fx, BorderLayout.CENTER);

		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				wv = new WebView();
				fx.setScene(new Scene(wv));
				engine = wv.getEngine();
				String url = txtURL.getText();
				engine.load(url);
			}
		});

		JButton btnGo = new JButton("Go");
		btnGo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						String url = txtURL.getText();
						if(url.contains("https://") || url.contains("http://")) {
							
						}else {
							url = "http://" + url;
						}
//						System.out.println();
						engine.load(url);
						engine.getLoadWorker().stateProperty().addListener((observable, oldState, newState) -> {
				            if (newState == Worker.State.SUCCEEDED){
				                txtURL.setText(engine.getLocation());
				            }
				        });
					}
				});
			}
		});
		panel.add(btnGo);
		DefaultComboBoxModel<String>  df = new DefaultComboBoxModel();
		comboBox.setModel(df);
		JButton btnSave = new JButton("");
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				links.add(engine.getLocation());
				df.addAll(links);
				
			}
		});
		
		JButton btnReload = new JButton("Reload");
		btnReload.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						engine.reload();
					}
				});
			}
		});
		panel.add(btnReload);
		panel.add(btnSave);
		ImageIcon icon = new ImageIcon("C:\\Users\\PHUOC\\eclipse-workspace\\fxBrowser\\src\\fxBrowser\\icon.png");
		btnSave.setIcon(icon);
		
	}
	
	@Override
    public void initialize(URL location, ResourceBundle resources) {
        engine = wv.getEngine();
        engine.getLoadWorker().stateProperty().addListener((observable, oldState, newState) -> {
            if (newState == Worker.State.SUCCEEDED){
                txtURL.setText(engine.getLocation());
            }
        });
    }

}
