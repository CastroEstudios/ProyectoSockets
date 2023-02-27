/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.mycompany.proyectosockets;

import Anadir.Anadir;
import com.formdev.flatlaf.FlatDarkLaf;
import java.awt.Image;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author 2dama
 */
public class CRUDSockets extends javax.swing.JFrame {

    public static String mandar = "equipos";
    private final Object lock = new Object();
    Connection con;

    public CRUDSockets() throws SQLException {
        initComponents();

        System.out.println("Conección correcta!");
        jComboBoxTablas.addItem("estadisticas");
        jComboBoxTablas.addItem("partidos");
        this.setLocationRelativeTo(this);

        try {
            iniciarConexion();
        } catch (SQLException ex) {
            
        }
        iconoABoton("src\\main\\java\\resources\\add.png",Anadir, "    Añadir     ");
        iconoABoton("src\\main\\java\\resources\\update.png",Actualizar, "Actualizar");
        iconoABoton("src\\main\\java\\resources\\borrar.png",Borrar, "Borrar");
        iconoABoton("src\\main\\java\\resources\\buscar.png",Buscar, "    Buscar");
        iconoABoton("src\\main\\java\\resources\\limpiar.png",Limpiar,"Limpiar Filtro");
        
        RefrescarTabla();
    }

    public Connection iniciarConexion() throws SQLException {
        con = DriverManager.getConnection("jdbc:mariadb://localhost:3306/sockets", "root", "");
        return con;
    }

    

    private void iconoABoton(String icono, JButton boton, String texto) {

        ImageIcon icon = new ImageIcon(icono);
        Image image = icon.getImage();
        int width = boton.getPreferredSize().width;
        int height = boton.getPreferredSize().height;
        Image resizedImage = image.getScaledInstance(28, 28, Image.SCALE_SMOOTH);
        ImageIcon resizedIcon = new ImageIcon(resizedImage);

        boton.setIcon(resizedIcon);
        boton.setText(texto);
        this.add(boton);

    }

    private void RefrescarTabla() throws SQLException {
        synchronized(lock) {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException ex) {
                Logger.getLogger(CRUDSockets.class.getName()).log(Level.SEVERE, null, ex);
            }
            Connection con = iniciarConexion();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("Select * from " + jComboBoxTablas.getSelectedItem() + " ");
            ResultSetMetaData rsmd = rs.getMetaData();

            DefaultTableModel dtm = (DefaultTableModel) Tabla.getModel();
            dtm.setRowCount(0);
            String[] cabecera = new String[rsmd.getColumnCount()];
            String[] info = new String[rsmd.getColumnCount()];

            for (int i = 1; i < rsmd.getColumnCount(); i++) {
                cabecera[i - 1] = rsmd.getColumnName(i);
            }

            dtm.setColumnIdentifiers(cabecera);

            for (int i = 0; i < rsmd.getColumnCount(); i++) {
                cabecera[i] = rsmd.getColumnName(i + 1);
                dtm.setColumnIdentifiers(cabecera);
            }

            while (rs.next()) {
                for (int i = 0; i < rsmd.getColumnCount(); i++) {
                    info[i] = rs.getString(i + 1);
                }
                dtm.addRow(info);
            }
            Tabla.setModel(dtm);
        }
    }

    private void BuscarEnTabla() throws SQLException {
        synchronized(lock) {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException ex) {
                Logger.getLogger(CRUDSockets.class.getName()).log(Level.SEVERE, null, ex);
            }
            if(jComboBoxTablas.getSelectedItem().toString().equals("jugadores") || jComboBoxTablas.getSelectedItem().toString().equals("equipos")) {
                Connection con = iniciarConexion();
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery("Select * from " + jComboBoxTablas.getSelectedItem() + " WHERE Nombre LIKE '" + JTextFieldNombre.getText() + "%' ");
                ResultSetMetaData rsmd = rs.getMetaData();

                DefaultTableModel dtm = (DefaultTableModel) Tabla.getModel();
                dtm.setRowCount(0);
                String[] cabecera = new String[rsmd.getColumnCount()];
                String[] info = new String[rsmd.getColumnCount()];

                for (int i = 1; i < rsmd.getColumnCount(); i++) {
                    cabecera[i - 1] = rsmd.getColumnName(i);
                }

                dtm.setColumnIdentifiers(cabecera);

                for (int i = 0; i < rsmd.getColumnCount(); i++) {
                    cabecera[i] = rsmd.getColumnName(i + 1);
                    dtm.setColumnIdentifiers(cabecera);
                }

                while (rs.next()) {
                    for (int i = 0; i < rsmd.getColumnCount(); i++) {
                        info[i] = rs.getString(i + 1);
                    }
                    dtm.addRow(info);
                }
                Tabla.setModel(dtm);   
            }else {
                JOptionPane.showMessageDialog(this, "Función solo permitida para jugadores y equipos.", "Error, tabla incorrecta", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void BorrarCampo() {
        synchronized(lock) {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException ex) {
                Logger.getLogger(CRUDSockets.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                Connection con = iniciarConexion();
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery("Select * from " + jComboBoxTablas.getSelectedItem() + " ");
                ResultSetMetaData rsmd = rs.getMetaData();

                st.executeUpdate("DELETE FROM " + jComboBoxTablas.getSelectedItem() + " WHERE " + rsmd.getColumnName(1) + " = '" + Tabla.getValueAt(Tabla.getSelectedRow(), 0) + "'  ");
                JOptionPane.showMessageDialog(this, "El registro se borró con éxito", "Borrado con éxito", JOptionPane.INFORMATION_MESSAGE);

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Borre en primer lugar los registros relacionados con el registro que esté intentando borrar", "ERROR de integridad de los datos", JOptionPane.ERROR_MESSAGE);
                Logger.getLogger(CRUDSockets.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void ActualizarCampo() {
        synchronized(lock) {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException ex) {
                Logger.getLogger(CRUDSockets.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                Connection con = iniciarConexion();
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery("Select * from " + jComboBoxTablas.getSelectedItem().toString() + " ");
                ResultSetMetaData rsmd = rs.getMetaData();

                for (int i = 1; i < rsmd.getColumnCount(); i++) {
                    Object objeto = Tabla.getValueAt(Tabla.getSelectedRow(), i);
                    st.executeUpdate("UPDATE " + jComboBoxTablas.getSelectedItem() + " "
                            + " SET " + rsmd.getColumnName(i + 1) + " = '" + objeto
                            + "' WHERE " + rsmd.getColumnName(1) + " = '"
                            + Tabla.getValueAt(Tabla.getSelectedRow(), 0) + "'");
                }
                JOptionPane.showMessageDialog(this, "El registro se actualizó con éxito", "Actualizado con éxito", JOptionPane.DEFAULT_OPTION);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Actualice en primer lugar los registros relacionados con el registro que esté intentando actualizar", "ERROR de integridad de los datos", JOptionPane.ERROR_MESSAGE);
                System.out.println(ex.getLocalizedMessage());
            }
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        Anadir = new javax.swing.JButton();
        jComboBoxTablas = new javax.swing.JComboBox<>();
        jScrollPane1 = new javax.swing.JScrollPane();
        Tabla = new javax.swing.JTable();
        Actualizar = new javax.swing.JButton();
        Borrar = new javax.swing.JButton();
        JTextFieldNombre = new javax.swing.JTextField();
        Limpiar = new javax.swing.JButton();
        Buscar = new javax.swing.JButton();
        statsJugador = new javax.swing.JButton();
        botonTopJugadores = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        Anadir.setText("jButton1");
        Anadir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AnadirActionPerformed(evt);
            }
        });

        jComboBoxTablas.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "equipos", "jugadores" }));
        jComboBoxTablas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxTablasActionPerformed(evt);
            }
        });

        Tabla.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(Tabla);

        Actualizar.setText("jButton1");
        Actualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ActualizarActionPerformed(evt);
            }
        });

        Borrar.setText("jButton1");
        Borrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BorrarActionPerformed(evt);
            }
        });

        JTextFieldNombre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JTextFieldNombreActionPerformed(evt);
            }
        });

        Limpiar.setText("Limpiar filtro");
        Limpiar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                LimpiarActionPerformed(evt);
            }
        });

        Buscar.setText("jButton1");
        Buscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BuscarActionPerformed(evt);
            }
        });

        statsJugador.setText("Stats por Jugador");
        statsJugador.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                statsJugadorActionPerformed(evt);
            }
        });

        botonTopJugadores.setText("Jugadores más altos");
        botonTopJugadores.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonTopJugadoresActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 743, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(196, 196, 196)
                        .addComponent(JTextFieldNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(Buscar, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(Limpiar, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(40, 40, 40)
                        .addComponent(jComboBoxTablas, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(57, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(Anadir, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(Actualizar, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(Borrar, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(botonTopJugadores, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(statsJugador, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(27, 27, 27))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(36, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Limpiar, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(JTextFieldNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBoxTablas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Buscar, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(Anadir, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(40, 40, 40)
                        .addComponent(Actualizar, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(40, 40, 40)
                        .addComponent(Borrar, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(botonTopJugadores, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(39, 39, 39)
                        .addComponent(statsJugador, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(14, 14, 14))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jComboBoxTablasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxTablasActionPerformed
        try {
            // TODO add your handling code here:
            RefrescarTabla();
        } catch (SQLException ex) {
            ex.getLocalizedMessage();
        }
        mandar = jComboBoxTablas.getSelectedItem().toString();
    }//GEN-LAST:event_jComboBoxTablasActionPerformed

    private void AnadirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AnadirActionPerformed
        try {
            // TODO add your handling code here:
            Anadir a = new Anadir(this, true);
            a.setVisible(true);
            RefrescarTabla();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error cargando los datos de la base de datos", "ERROR de cargado", JOptionPane.ERROR_MESSAGE);
            Logger.getLogger(CRUDSockets.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_AnadirActionPerformed

    private void ActualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ActualizarActionPerformed
        // TODO add your handling code here:
        if (Tabla.getSelectionModel().isSelectionEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor seleccione un valor en la tabla antes de darle al botón", "Mete algo", JOptionPane.WARNING_MESSAGE);
        } else {
            ActualizarCampo();
            try {
                RefrescarTabla();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error cargando los datos de la base de datos", "ERROR de cargado", JOptionPane.ERROR_MESSAGE);
                Logger.getLogger(CRUDSockets.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_ActualizarActionPerformed

    private void BorrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BorrarActionPerformed
        // TODO add your handling code here:
        if (Tabla.getSelectionModel().isSelectionEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor seleccione un valor en la tabla antes de darle al botón", "Mete algo", JOptionPane.WARNING_MESSAGE);
        } else {
            BorrarCampo();
            try {
                RefrescarTabla();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error cargando los datos de la base de datos", "ERROR de cargado", JOptionPane.ERROR_MESSAGE);
                Logger.getLogger(CRUDSockets.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_BorrarActionPerformed

    private void JTextFieldNombreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JTextFieldNombreActionPerformed
        // TODO add your handling code here:
        if(JTextFieldNombre.getText()== null){
            try {
                RefrescarTabla();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error cargando los datos de la base de datos", "ERROR de cargado", JOptionPane.INFORMATION_MESSAGE);
                Logger.getLogger(CRUDSockets.class.getName()).log(Level.SEVERE, null, ex);
            }
        }else{
            try {
                BuscarEnTabla();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Datos de búsqueda no reconocidos", "ERROR de búsqueda", JOptionPane.ERROR_MESSAGE);
                Logger.getLogger(CRUDSockets.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_JTextFieldNombreActionPerformed

    private void LimpiarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_LimpiarActionPerformed
        try {
            // TODO add your handling code here:
            RefrescarTabla();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error cargando los datos de la base de datos", "ERROR de cargado", JOptionPane.INFORMATION_MESSAGE);
            Logger.getLogger(CRUDSockets.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_LimpiarActionPerformed

    private void BuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BuscarActionPerformed
        try {
            // TODO add your handling code here:
            BuscarEnTabla();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error cargando los datos de la base de datos", "ERROR de cargado", JOptionPane.INFORMATION_MESSAGE);
            Logger.getLogger(CRUDSockets.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_BuscarActionPerformed

    private void statsJugadorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_statsJugadorActionPerformed
        // TODO add your handling code here:
        synchronized(lock) {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException ex) {
                Logger.getLogger(CRUDSockets.class.getName()).log(Level.SEVERE, null, ex);
            }
            if(jComboBoxTablas.getSelectedItem().toString().equals("estadisticas")) {
                try {
                    Connection con = iniciarConexion();
                    Statement st = con.createStatement();
                    ResultSet rs = st.executeQuery("""
                                                   SELECT jugador, temporada, Puntos_por_partido, Asistencias_por_partido, Tapones_por_partido, Rebotes_por_partido
                                                   FROM estadisticas
                                                   GROUP BY jugador
                                                   ORDER BY temporada ASC""");
                    ResultSetMetaData rsmd = rs.getMetaData();

                    DefaultTableModel dtm = (DefaultTableModel) Tabla.getModel();
                    dtm.setRowCount(0);
                    String[] cabecera = new String[rsmd.getColumnCount()];
                    String[] info = new String[rsmd.getColumnCount()];

                    for (int i = 1; i < rsmd.getColumnCount(); i++) {
                        cabecera[i - 1] = rsmd.getColumnName(i);
                    }

                    dtm.setColumnIdentifiers(cabecera);

                    for (int i = 0; i < rsmd.getColumnCount(); i++) {
                        cabecera[i] = rsmd.getColumnName(i + 1);
                        dtm.setColumnIdentifiers(cabecera);
                    }

                    while (rs.next()) {
                        for (int i = 0; i < rsmd.getColumnCount(); i++) {
                            info[i] = rs.getString(i + 1);
                        }
                        dtm.addRow(info);
                    }
                    Tabla.setModel(dtm); 
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this, "No sé como has conseguido este error, pero has ganado un achievement", "ERROR?!", JOptionPane.INFORMATION_MESSAGE);
                    Logger.getLogger(CRUDSockets.class.getName()).log(Level.SEVERE, null, ex);
                }
            }else {
                JOptionPane.showMessageDialog(this, "Seleccione la tabla Estadísticas en primer lugar...", "Error, tabla incorrecta", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_statsJugadorActionPerformed

    private void botonTopJugadoresActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonTopJugadoresActionPerformed
        // TDO add your handling code here:
        synchronized(lock) {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException ex) {
                Logger.getLogger(CRUDSockets.class.getName()).log(Level.SEVERE, null, ex);
            }
            if(jComboBoxTablas.getSelectedItem().toString().equals("jugadores")) {
                try {
                    Connection con = iniciarConexion();
                    Statement st = con.createStatement();
                    ResultSet rs = st.executeQuery("""
                                                   SELECT Nombre, Altura, Nombre_equipo
                                                   FROM jugadores
                                                   GROUP BY Nombre_equipo
                                                   ORDER BY Altura DESC""");
                    ResultSetMetaData rsmd = rs.getMetaData();

                    DefaultTableModel dtm = (DefaultTableModel) Tabla.getModel();
                    dtm.setRowCount(0);
                    String[] cabecera = new String[rsmd.getColumnCount()];
                    String[] info = new String[rsmd.getColumnCount()];

                    for (int i = 1; i < rsmd.getColumnCount(); i++) {
                        cabecera[i - 1] = rsmd.getColumnName(i);
                    }

                    dtm.setColumnIdentifiers(cabecera);

                    for (int i = 0; i < rsmd.getColumnCount(); i++) {
                        cabecera[i] = rsmd.getColumnName(i + 1);
                        dtm.setColumnIdentifiers(cabecera);
                    }

                    while (rs.next()) {
                        for (int i = 0; i < rsmd.getColumnCount(); i++) {
                            info[i] = rs.getString(i + 1);
                        }
                        dtm.addRow(info);
                    }
                    Tabla.setModel(dtm); 
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this, "No sé como has conseguido este error, pero has ganado un achievement", "ERROR?!", JOptionPane.INFORMATION_MESSAGE);
                    Logger.getLogger(CRUDSockets.class.getName()).log(Level.SEVERE, null, ex);
                }
            }else {
                JOptionPane.showMessageDialog(this, "Seleccione la tabla Jugadores en primer lugar...", "Error, tabla incorrecta", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_botonTopJugadoresActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;

                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(CRUDSockets.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(CRUDSockets.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(CRUDSockets.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(CRUDSockets.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        FlatDarkLaf.setup();
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new CRUDSockets().setVisible(true);
                } catch (SQLException ex) {
                    Logger.getLogger(CRUDSockets.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Actualizar;
    private javax.swing.JButton Anadir;
    private javax.swing.JButton Borrar;
    private javax.swing.JButton Buscar;
    private javax.swing.JTextField JTextFieldNombre;
    private javax.swing.JButton Limpiar;
    private javax.swing.JTable Tabla;
    private javax.swing.JButton botonTopJugadores;
    private javax.swing.JComboBox<String> jComboBoxTablas;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton statsJugador;
    // End of variables declaration//GEN-END:variables
}
