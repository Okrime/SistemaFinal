package view;

import java.sql.Connection;
import java.sql.ResultSet;

import javax.swing.JOptionPane;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Label;
import org.eclipse.wb.swt.SWTResourceManager;

import com.mysql.jdbc.Statement;

import conexao.connect;

import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class janelaAlterarLogin extends Shell {
	private Text text;

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String args[]) {
		try {
			Display display = Display.getDefault();
			janelaAlterarLogin shell = new janelaAlterarLogin(display);
			shell.open();
			shell.layout();
			while (!shell.isDisposed()) {
				if (!display.readAndDispatch()) {
					display.sleep();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the shell.
	 * @param display
	 */
	public janelaAlterarLogin(Display display) {
		super(display, SWT.SHELL_TRIM);
		
		Label lblEntreComO = new Label(this, SWT.NONE);
		lblEntreComO.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.BOLD));
		lblEntreComO.setBounds(10, 10, 256, 30);
		lblEntreComO.setText("Entre com o Login a ser alterado:");
		
		text = new Text(this, SWT.BORDER);
		text.setBounds(33, 46, 181, 21);
		
		Button btnContinuar = new Button(this, SWT.NONE);
		btnContinuar.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				boolean valid = false;
				String user = text.getText();
				valid = menu.checarUser(user);	//validar se login existe
				setVisible(false);
				if (valid){	//se retornar true eh pq achou login
					try
					{
						Connection ExConn = connect.getConnection();
						Statement stmt = (Statement) ExConn.createStatement();
						String sqlBusca = "SELECT * FROM new_schema.login WHERE loginnome = '" + user + "';";
						ResultSet rs = stmt.executeQuery(sqlBusca);
						rs.next();
						int loggedAux;	//tipo de login
						loggedAux = rs.getInt(4);
						int loggedIdAux;//id do login
						loggedIdAux = rs.getInt(1);
						int tipoPessoa = 1;
						if (loggedAux == 1){
							sqlBusca = "SELECT * FROM new_schema.cliente WHERE clientenome = '" + user + "';";
							rs = stmt.executeQuery(sqlBusca);
							rs.next();
							tipoPessoa = rs.getInt(6);
						}
						System.out.println(tipoPessoa);
						switch(loggedAux){
						case 1: if (tipoPessoa == 1){	//se for 1 eh pessoa fisica
									janelaFuncionarioAltClienteFis jfacf = new janelaFuncionarioAltClienteFis(display);
									jfacf.setVisible(true);
								} else {				//se for 2 eh pessoa juridica
									janelaFuncionarioAltClienteJur jfacj = new janelaFuncionarioAltClienteJur(display);
									jfacj.setVisible(true);
								}
							break;
						case 2:	janelaGerenteAltFuncionario jgaf = new janelaGerenteAltFuncionario(display);
							jgaf.setVisible(true);
							break;
						case 3: janelaGerenteAltFuncionario jgaf2 = new janelaGerenteAltFuncionario(display);
							jgaf2.setVisible(true);
							break;
						}
					}
					catch (Exception j)
					{
						System.out.println("Erro.");
					}
				}else {
					operacaoFalha oF = new operacaoFalha(display);
					oF.setVisible(true);
				}
			}
		});
		btnContinuar.setBounds(33, 86, 75, 25);
		btnContinuar.setText("Continuar");
		
		Button btnVoltar = new Button(this, SWT.NONE);
		btnVoltar.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				dispose();
			}
		});
		btnVoltar.setBounds(139, 86, 75, 25);
		btnVoltar.setText("Voltar");
		
		createContents();
	}

	/**
	 * Create contents of the shell.
	 */
	protected void createContents() {
		setText("Entre com o Login");
		setSize(281, 160);

	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

}
