package com.tagmycode.plugin.gui;

import com.tagmycode.plugin.Framework;
import com.tagmycode.plugin.GuiThread;
import com.tagmycode.plugin.ICallback;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SettingsForm {
    private JPanel mainPanel;
    private JPanel loginPanel;
    private JPanel logoutPanel;
    private JButton loginButton;
    private JButton logoutButton;
    private JLabel identity;

    private Framework framework;

    public SettingsForm(final Framework framework) {
        this.framework = framework;
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                framework.showAuthenticateDialog(new ICallback() {
                    @Override
                    public void doOperation() {
                        refreshPanelInGuiThread();
                    }
                });
            }
        });

        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                framework.logout();
                refreshPanelInGuiThread();
            }
        });
        doRefreshPanel();
    }

    private void refreshPanelInGuiThread() {
        new GuiThread().execute(new Runnable() {
            @Override
            public void run() {
                doRefreshPanel();
            }
        });

    }

    private void doRefreshPanel() {
        if (framework.isInitialized()) {
            identity.setText(framework.getAccount().getEmail());
            loginPanel.setVisible(false);
            logoutPanel.setVisible(true);
        } else {
            loginPanel.setVisible(true);
            logoutPanel.setVisible(false);
        }
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public JPanel getLoginPanel() {
        return loginPanel;
    }

    public JPanel getLogoutPanel() {
        return logoutPanel;
    }

    public JButton getLoginButton() {
        return loginButton;
    }

    public JButton getLogoutButton() {
        return logoutButton;
    }
}
