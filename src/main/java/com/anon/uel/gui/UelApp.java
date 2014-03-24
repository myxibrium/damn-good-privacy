package com.anon.uel.gui;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.security.KeyPair;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

import com.anon.dgp.encoding.DgpEncoded;
import com.anon.dgp.encryption.DgpDecryptionRequest;
import com.anon.dgp.encryption.DgpEncryptionRequest;
import com.anon.dgp.encryption.DgpEncryptionService;
import com.anon.dgp.keygen.DgpKeyGenerationResponse;
import com.anon.dgp.keygen.DgpKeyGenerationService;
import com.anon.uel.digest.Sha256Digest;
import com.anon.uel.encoding.Decoder;
import com.anon.uel.encoding.Section;
import com.anon.uel.encryption.AsymmetricKeyGenerator;
import com.anon.uel.encryption.SecurityProviderInitializer;
import com.anon.uel.encryption.ec.EllipticCurveAsymmetricKeyGenerator;
import com.anon.uel.encryption.ec.dh.EcDhAgreementGenerator;
import com.anon.uel.encryption.keys.GenericKeyGenerator;
import com.anon.uel.encryption.serpent.SerpentEncryptor;

public class UelApp {
	
	private static DgpEncryptionService dgpEncryptionService;
	private static DgpKeyGenerationService dgpKeygenService;

    public static void main(String[] args) {
    		SecurityProviderInitializer.initializeSecurityProviders();
    		dgpEncryptionService = new DgpEncryptionService();
    		dgpKeygenService = new DgpKeyGenerationService();
    		
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
    
	private static void createAndShowGUI() {
        final JFrame frame = new JFrame("DGP - Damn Good Privacy");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        final JPanel backPanel = new JPanel();
        backPanel.setLayout(new BoxLayout(backPanel, BoxLayout.Y_AXIS));
        frame.getContentPane().add(backPanel);

        final JButton genKeyPairs = new JButton("Generate Key Pairs");
        backPanel.add(genKeyPairs);
        
        genKeyPairs.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				showKeyGenerationGui();
			}
		});

        final JButton encryptMessage = new JButton("Encrypt Message");
        backPanel.add(encryptMessage);
        
        encryptMessage.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				showEncryptionGui();
			}
		});

        final JButton decryptMessage = new JButton("Decrypt Message");
        backPanel.add(decryptMessage);
        
        decryptMessage.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				showDecryptionGui();
			}
		});

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }
	
	private static void showEncryptionGui() {
        final JFrame frame = new JFrame("DGP Encryption");
        
        final JPanel backPanel = new JPanel();
        backPanel.setLayout(new BoxLayout(backPanel, BoxLayout.Y_AXIS));
        frame.getContentPane().add(backPanel);
        
        final JPanel encryptPanel = buildEncryptPanel();
        backPanel.add(encryptPanel);
        
        frame.pack();
        frame.setVisible(true);
	}
	
	private static void showDecryptionGui() {
		final JFrame frame = new JFrame("DGP Decryption");
		
		final JPanel backPanel = new JPanel();
		backPanel.setLayout(new BoxLayout(backPanel, BoxLayout.Y_AXIS));
		frame.getContentPane().add(backPanel);
		
		final JPanel encryptPanel = buildDecryptPanel();
		backPanel.add(encryptPanel);
		
		frame.pack();
		frame.setVisible(true);
	}
	
	private static void showKeyGenerationGui() {
        final JFrame frame = new JFrame("DGP Key Generation");
        
        final JPanel backPanel = new JPanel();
        backPanel.setLayout(new BoxLayout(backPanel, BoxLayout.Y_AXIS));
        frame.getContentPane().add(backPanel);
        
        final JPanel asymKeyPanel = buildAsymKeyPanel();
        backPanel.add(asymKeyPanel);
        
        frame.pack();
        frame.setVisible(true);
	}

	private static JPanel buildDecryptPanel() {
		final JPanel decryptPanel = new JPanel();
		decryptPanel.setLayout(new BoxLayout(decryptPanel, BoxLayout.Y_AXIS));
        
        final JLabel messageLabel = new JLabel("DGP Message Block:");
        decryptPanel.add(messageLabel);
        
        final JTextArea message = new JTextArea(5, 5);
        decryptPanel.add(message);
        addScrollBars(decryptPanel, message);
        
        final JLabel privateKeyLabel = new JLabel("My Private Key:");
        decryptPanel.add(privateKeyLabel);
        
        final JTextArea privateKey = new JTextArea(5, 5);
        decryptPanel.add(privateKey);
        addScrollBars(decryptPanel, privateKey);
        
        final JLabel publicKeyLabel = new JLabel("Their Public Key:");
        decryptPanel.add(publicKeyLabel);
        
        final JTextArea publicKey = new JTextArea(5, 10);
        decryptPanel.add(publicKey);
        addScrollBars(decryptPanel, publicKey);

        final JButton encrypt = new JButton("Decrypt");
        decryptPanel.add(encrypt);
        
        final JLabel plainTextLabel = new JLabel("Plain Text:");
        decryptPanel.add(plainTextLabel);
        
        final JTextArea plainText = new JTextArea(5, 10);
        decryptPanel.add(plainText);
        addScrollBars(decryptPanel, plainText);
        
        encrypt.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				DgpDecryptionRequest request = new DgpDecryptionRequest();
				request.setMyPrivateKey(privateKey.getText());
				request.setTheirPublicKey(publicKey.getText());
				request.setMessage(message.getText());
				plainText.setText(new String(dgpEncryptionService.decrypt(request)));
			}
		});
		
		return decryptPanel;
	}

	private static JPanel buildEncryptPanel() {
		final JPanel encryptPanel = new JPanel();
		encryptPanel.setLayout(new BoxLayout(encryptPanel, BoxLayout.Y_AXIS));
        
        final JLabel plainTextLabel = new JLabel("Plain Text:");
        encryptPanel.add(plainTextLabel);
        
        final JTextArea plainText = new JTextArea(5, 5);
        encryptPanel.add(plainText);
        addScrollBars(encryptPanel, plainText);
        
        final JLabel privateKeyLabel = new JLabel("My Private Key:");
        encryptPanel.add(privateKeyLabel);
        
        final JTextArea privateKey = new JTextArea(5, 5);
        encryptPanel.add(privateKey);
        addScrollBars(encryptPanel, privateKey);
        
        final JLabel publicKeyLabel = new JLabel("Their Public Key:");
        encryptPanel.add(publicKeyLabel);
        
        final JTextArea publicKey = new JTextArea(5, 10);
        encryptPanel.add(publicKey);
        addScrollBars(encryptPanel, publicKey);

        final JButton encrypt = new JButton("Encrypt");
        encryptPanel.add(encrypt);
        
        final JLabel encryptedLabel = new JLabel("Encrypted Text:");
        encryptPanel.add(encryptedLabel);
        
        final JTextArea encryptedText = new JTextArea(5, 10);
        encryptPanel.add(encryptedText);
        addScrollBars(encryptPanel, encryptedText);
        
        encrypt.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				DgpEncryptionRequest request = new DgpEncryptionRequest();
				request.setMyPrivateKey(privateKey.getText());
				request.setTheirPublicKey(publicKey.getText());
				request.setPlainBytes(plainText.getText().getBytes());
				encryptedText.setText(dgpEncryptionService.encrypt(request));
			}
		});
		
		return encryptPanel;
	}

	private static void addScrollBars(final JPanel container,
			final Component component) {
		JScrollPane scroll = new JScrollPane(component);
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        container.add(scroll);
	}

	private static JPanel buildAsymKeyPanel() {
		final JPanel asymKeyPanel = new JPanel();
        asymKeyPanel.setLayout(new BoxLayout(asymKeyPanel, BoxLayout.Y_AXIS));

        final JButton generateKeys = new JButton("Generate Public/Private Keys");
        asymKeyPanel.add(generateKeys);
        
        final JLabel publicKeyLabel = new JLabel("Public Key:");
        asymKeyPanel.add(publicKeyLabel);
        
        final JTextArea publicKey = new JTextArea(5, 5);
        asymKeyPanel.add(publicKey);
        addScrollBars(asymKeyPanel, publicKey);
        
        final JLabel privateKeyLabel = new JLabel("Private Key:");
        asymKeyPanel.add(privateKeyLabel);
        
        final JTextArea privateKey = new JTextArea(5, 5);
        asymKeyPanel.add(privateKey);
        addScrollBars(asymKeyPanel, privateKey);
        
        generateKeys.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				DgpKeyGenerationResponse keys = dgpKeygenService.generateKeys();
				publicKey.setText(keys.getPublicKeys());
				privateKey.setText(keys.getPrivateKeys());
				
			}
		});
		return asymKeyPanel;
	}
	
}
