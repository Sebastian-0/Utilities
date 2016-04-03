/*
 * Copyright (C) 2016 Sebastian Hjelm
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 */

package sutilities;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.net.URL;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import javax.swing.plaf.basic.BasicScrollBarUI;

/**
 * A class that represents a crash dialog. The crash dialog is displayed when
 *  the program encounters an internal error, causing it to crash. The dialog
 *  is non-modal, meaning that it doesn't freeze the thread that invoked
 *  {@code setVisible(true)}. Instead use {@link #isVisible()} to check if the
 *  dialog is still open, this will be set to <code>false</code> once the
 *  dialog closes.
 * </br>
 * </br>The dialog also includes a feature to send error reports to the
 *  developers of the software, but this feature is only available if a webb
 *  address has been specified.
 * @author Sebastian Hjelm
 */
@SuppressWarnings("serial")
public class CrashDialog extends JFrame
{
  private static Color bkg    = new Color(104, 104, 104);
  private static Color border = new Color(64, 64, 64);
  private static Color textBkg        = new Color(206, 206, 206);
  private static Color labelBkgBright = textBkg;
  private static Color labelBkgDark   = new Color(120, 120, 120);
  private static Color labelFgRed     = new Color(206, 42, 37);
  private static Color labelFgDark    = new Color(34, 34, 34);
  private static Color buttonBkg      = textBkg;
  
  
  private URL errorAddress;
  
  private JButton close;
  private JButton sendReport;
  
  private int mX;
  private int mY;
  
  private String errorText;
  
  
  /**
   * Creates a new {@code CrashDialog} describing the specified error. The cause
   *  of the error is optional, but will make an error report more useful.
   * @param location The location of the error (class and method)
   * @param error The error message itself
   * @param cause The cause of the error (optional)
   */
  public CrashDialog(String location, String error, Throwable cause)
  {
    this (location,
        error,
        cause,
        "Internal Error",
        "<html>An error occured while executing the program." +
        " Please consider sending an error report to the" +
        "<br />developers, to do so just click on the button below." + 
        "<br /><br />We are sorry for your inconvenience!</html>",
        "Technical details",
        "Close",
        "Send error report");
  }
  
  /**
   * Creates a new {@code CrashDialog} describing the specified error. The cause
   *  of the error is optional, but will make an error report more useful.
   * </br>
   * </br>This constructor also allows the caller to specify all the text that
   *  is displayed within the dialog. The title and message describe the top two
   *  fields of the dialog while the details title is the title of the details
   *  text area. The close and error report texts are the text on those buttons.
   * @param location The location of the error (class and method)
   * @param error The error message itself
   * @param cause The cause of the error (optional)
   * @param dialogTitle The title of the dialog
   * @param dialogMessage The main message of the dialog, displayed at the top
   * @param detailsTitle The title for the detailed information
   * @param closeText The text on the close-button
   * @param errorReportText The text on the error report-button
   */
  public CrashDialog(String location, String error, Throwable cause,
      String dialogTitle, String dialogMessage, String detailsTitle,
      String closeText, String errorReportText)
  {
    // TODO CrashDialog; Add an icon
    setTitle(dialogTitle);
    
    getContentPane().setBackground(bkg);
    
    JLabel title = new CLabel(dialogTitle, true, false);
    title.setFont(title.getFont().deriveFont(Font.BOLD, 32f));
    title.setForeground(labelFgDark);
    
    JLabel message = new CLabel(dialogMessage, false, true);
    message.setForeground(labelFgDark);
    message.setHorizontalAlignment(SwingConstants.CENTER);
    
    JLabel details = new CLabel(detailsTitle, true, true);
    details.setForeground(labelFgDark);
    details.setFont(details.getFont().deriveFont(14f));
    
    JTextArea   textArea = new CTextArea(location + ": " + error, cause);
    JScrollPane scroll   = new CScrollPane();
    scroll.setViewportView(textArea);
    scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
    
    errorText = textArea.getText();
    
    close      = new CButton(closeText, actionListener);
    sendReport = new CButton(errorReportText, actionListener);

    JPanel panel = new JPanel();
    panel.setBackground(bkg);
    panel.setBorder(new CompoundBorder(new LineBorder(border), new EmptyBorder(5, 5, 5, 5)));
    panel.addMouseListener      (mouseListener);
    panel.addMouseMotionListener(mouseMotionListener);
    
    GridBagLayout      gbl = new GridBagLayout();
    GridBagConstraints gc  = new GridBagConstraints();
    panel.setLayout(gbl);
    
    gc.insets.set(5, 5, 5, 5);
    
    addToGrid(panel, title     , gbl, gc, 0, 0, 2, 1, GridBagConstraints.NONE, 0, 0);
    addToGrid(panel, message   , gbl, gc, 0, 1, 2, 1, GridBagConstraints.HORIZONTAL, 0, 0);
    gc.insets.set(20, 5, 0, 5);
    addToGrid(panel, details   , gbl, gc, 0, 2, 2, 1, GridBagConstraints.NONE, 0, 0, GridBagConstraints.WEST);
    gc.insets.set(0, 5, 5, 5);
    addToGrid(panel, scroll    , gbl, gc, 0, 3, 2, 1, GridBagConstraints.BOTH, 0, 0);
    gc.insets.set(5, 5, 5, 5);
    addToGrid(panel, close     , gbl, gc, 0, 4, 1, 1, GridBagConstraints.NONE, 0, 0);
    addToGrid(panel, sendReport, gbl, gc, 1, 4, 1, 1, GridBagConstraints.NONE, 0, 0, GridBagConstraints.WEST);
    
    add(panel);
    
    sendReport.setEnabled(false);
    
    setUndecorated(true);
    
    pack();
    setLocationRelativeTo(null);
  }
  
  private void addToGrid(JComponent target, Component comp, GridBagLayout l, GridBagConstraints c,
      int x, int y, int width, int height, int fill, double wx, double wy)
  {
    addToGrid(target, comp, l, c, x, y, width, height, fill, wx, wy, GridBagConstraints.CENTER);
  }

  private void addToGrid(JComponent target, Component comp, GridBagLayout l, GridBagConstraints c,
      int x, int y, int width, int height, int fill, double wx, double wy, int anchor)
  {
    c.gridx = x;
    c.gridy = y;
    c.gridwidth  = width;
    c.gridheight = height;
    c.weightx = wx;
    c.weighty = wy;
    c.anchor  = anchor;
    c.fill    = fill;
    
    l.setConstraints(comp, c);
    target.add(comp);
  }
  
  
  public void setErrorReportURL(URL url)
  {
    errorAddress = url;
    if (errorAddress != null)
      sendReport.setEnabled(true);
    else
      sendReport.setEnabled(false);
  }
  
  
  protected void sendErrorReport(URL errorAddress, String reportContents)
  {
  }


  private String generateReportText()
  {
    return errorText;
  }
  
  
  private static class CLabel extends JLabel
  {
    public CLabel(String text, boolean bright, boolean opaque)
    {
      super (text);
      setOpaque(opaque);
      setForeground(labelFgRed);
      if (opaque)
      {
        if (!bright)
        {
          setBackground(labelBkgDark);
          setBorder(new MatteBorder(3, 6, 3, 6, labelBkgDark));
        }
        else
        {
          setBackground(labelBkgBright);
          setBorder(new MatteBorder(3, 6, 3, 6, labelBkgBright));
        }
      }
    }
  }
  
  
  private static class CTextArea extends JTextArea
  {
    public CTextArea(String errorText, Throwable throwable)
    {
      super (15, 50);
      setEditable(false);
      setFont(new Font("Courier New", Font.PLAIN, 12));
      
      setBackground(new Color(206, 206, 206));
      setFont(getFont().deriveFont(Font.BOLD));
      setBorder(new MatteBorder(3, 3, 3, 3, new Color(206, 206, 206)));
      
      StringBuilder builder = new StringBuilder(errorText);
      
      if (throwable != null)
      {
        builder.append("\n\nException Stacktrace:\n");
        
        printData(builder, throwable);
      }
      
      Throwable cause = throwable.getCause();
      
      while (cause != null)
      {
        builder.append("\nCaused by:\n");

        printData(builder, cause);
        
        cause = cause.getCause();
      }
      
      setText(builder.toString());
    }
    
    
    private void printData(StringBuilder builder, Throwable throwable)
    {
      StackTraceElement[] stack = throwable.getStackTrace();
      
      builder.append("   " + throwable.toString());
      for (int i = 0; i < stack.length; i++)
      {
        builder.append("\n         at ");
        builder.append(stack[i]);
      }
    }
  }
  
  private static class CScrollPane extends JScrollPane
  {
    public CScrollPane()
    {
      setBackground(textBkg);
      setOpaque(true);
      setBorder(new MatteBorder(2, 2, 2, 2, textBkg));
      
      getVerticalScrollBar  ().setUI(new ScrollUI());
      getHorizontalScrollBar().setUI(new ScrollUI());
    }
  }
  
  private static class CButton extends JButton
  {
    public CButton(String text, ActionListener listener)
    {
      super (text);
      addActionListener(listener);
      setBackground(buttonBkg);
      setFocusable(false);
    }
  }
  
  
  private static class ScrollUI extends BasicScrollBarUI
  {
    @Override
    protected JButton createIncreaseButton(int orientation)
    {
      return zeroBtn();
    }
    
    
    @Override
    protected JButton createDecreaseButton(int orientation)
    {
      return zeroBtn();
    }
    
    
    private JButton zeroBtn()
    {
      JButton btn = new JButton();
      btn.setPreferredSize(new Dimension(0, 0));
      btn.setMinimumSize(new Dimension(0, 0));
      btn.setMaximumSize(new Dimension(0, 0));
      return btn;
    }
    
    @Override
    protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds)
    {
      boolean vertical = true;
      if (((JScrollBar)c).getOrientation() == JScrollBar.HORIZONTAL)
        vertical = false;
      
      g.setColor(new Color(206, 206, 206));
      g.fillRect(trackBounds.x, trackBounds.y, trackBounds.width, trackBounds.height);
      g.setColor(new Color(176, 176, 176));
      
      if (!vertical)
        g.fillRect(trackBounds.x + trackBounds.height / 4, trackBounds.y + trackBounds.height / 4 + 1, trackBounds.width - trackBounds.height / 2, trackBounds.height / 2 - 2);
      else
        g.fillRect(trackBounds.x + trackBounds.width / 4 + 1, trackBounds.y + trackBounds.width / 4, trackBounds.width / 2 - 2, trackBounds.height - trackBounds.width / 2);
    }
    
    
    @Override
    protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds)
    {
      boolean vertical = true;
      if (((JScrollBar)c).getOrientation() == JScrollBar.HORIZONTAL)
        vertical = false;
      
      g.setColor(new Color(104, 104, 104));
      
      if (!vertical)
        g.fillRect(thumbBounds.x + 1, thumbBounds.y + 3, thumbBounds.width - 2, thumbBounds.height - 7);
      else
        g.fillRect(thumbBounds.x + 3, thumbBounds.y + 1, thumbBounds.width - 7, thumbBounds.height - 2);
    }
  }
  
  
  private ActionListener actionListener = new ActionListener()
  {
    @Override
    public void actionPerformed(ActionEvent e)
    {
      if (e.getSource() == close)
      {
        setVisible(false);
      }
      else if (e.getSource() == sendReport)
      {
        sendErrorReport(errorAddress, generateReportText());
      }
    }
  };
  
  
  private MouseListener mouseListener = new MouseAdapter() {
    @Override
    public void mousePressed(MouseEvent e)
    {
      mX = e.getX();
      mY = e.getY();
    }
  };
  
  
  private MouseMotionListener mouseMotionListener = new MouseAdapter() {
    @Override
    public void mouseDragged(MouseEvent e)
    {
      setLocation(getLocation().x + e.getX() - mX, getLocation().y + e.getY() - mY);
    }
  };
}
