package view;
import java.sql.Connection;
import java.sql.ResultSet;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.mysql.jdbc.Statement;

import conexao.connect;

import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import view.menu;

public class janelaClienteDepositar extends Shell {
	private Text text;
	private Text text_1;
	private Text text_2;

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String args[]) {
		try {
			Display display = Display.getDefault();
			janelaClienteDepositar shell = new janelaClienteDepositar(display);
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
	public janelaClienteDepositar(Display display) {
		super(display, SWT.SHELL_TRIM);
		
		Label lblConta = new Label(this, SWT.NONE);
		lblConta.setBounds(26, 13, 55, 15);
		lblConta.setText("Conta");
		
		Label lblAgencia = new Label(this, SWT.NONE);
		lblAgencia.setBounds(26, 52, 55, 15);
		lblAgencia.setText("Agencia");
		
		Label lblValor = new Label(this, SWT.NONE);
		lblValor.setBounds(26, 96, 55, 15);
		lblValor.setText("Valor");
		
		text = new Text(this, SWT.BORDER);
		text.setBounds(95, 10, 138, 21);
		
		text_1 = new Text(this, SWT.BORDER);
		text_1.setBounds(93, 52, 140, 21);
		
		text_2 = new Text(this, SWT.BORDER);
		text_2.setBounds(93, 96, 140, 21);
		
		Label lblTipoDeConta = new Label(this, SWT.NONE);
		lblTipoDeConta.setBounds(10, 141, 85, 15);
		lblTipoDeConta.setText("Tipo de Conta");
		
		Button btnCorrente = new Button(this, SWT.RADIO);
		btnCorrente.setBounds(109, 140, 67, 16);
		btnCorrente.setText("Corrente");
		
		Button btnPoupanca = new Button(this, SWT.RADIO);
		btnPoupanca.setBounds(189, 140, 74, 16);
		btnPoupanca.setText("Poupanca");
		
		Button btnCancelar = new Button(this, SWT.NONE);
		btnCancelar.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				dispose();
			}
		});
		btnCancelar.setBounds(215, 206, 75, 25);
		btnCancelar.setText("Cancelar");
		
		Label label_value = new Label(this, SWT.NONE);
		label_value.setBounds(131, 175, 45, 15);
		String aux;
		aux = String.valueOf(menu.saldo);
		label_value.setText(aux);
		
		Button btnConfirmar = new Button(this, SWT.NONE);
		btnConfirmar.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				boolean valid = false;
				
				try{
					Connection Conn = connect.getConnection();
					Statement stmt = (Statement) Conn.createStatement();
					String sqlBusca = "SELECT * FROM new_schema.cliente WHERE clienteid = " + menu.idLogado + ";";
					//Test later
					ResultSet rs = stmt.executeQuery(sqlBusca);
					rs.next();
					
					menu.saldo = rs.getFloat(5);
					
					
				}catch(Exception j){
					System.out.println("Erro.");
				}
				
				float valor;
				int a, c;
				String agencia = text_1.getText();
				a = Integer.parseInt(agencia);
				String conta = text.getText();
				c = Integer.parseInt(conta);
				String aux;
				aux = text_2.getText();
				valor = Float.parseFloat(aux);
				//agencia = getAgencia(id);
				valid = menu.checarAgencia(a);	//checar agencia
				if (valid){
					valid = menu.checarConta(c);	//checar conta
					if (valid)
						valid = menu.checarValor(menu.saldo, valor);	//validar saldo
				}
				setVisible(false);
				if (valid){
					//saldo = saldo - valor;	//* BD * atualizar valor do saldo do depositante
					//saldoAlvo = saldoAlvo + valor;	//* BD * atualizar valor do saldo da conta depositada
					try{
						Connection Conn = connect.getConnection();
						float saldo=0;
						Statement stmt = (Statement) Conn.createStatement();
						menu.saldo = menu.saldo - valor;	//* BD * atualizar saldo no BD
						String sqlUpdate = "UPDATE new_schema.cliente SET saldo = '" + menu.saldo + "' WHERE	clienteid = " + menu.idLogado + ";";
						stmt.executeUpdate(sqlUpdate);
						String sqlBusca = "SELECT * FROM new_schema.cliente WHERE conta = " + c + ";";
						ResultSet rs = stmt.executeQuery(sqlBusca);
						rs.next();
						
						saldo = rs.getFloat(5);
						saldo = saldo + valor;
						String sqlUpdate2 = "UPDATE new_schema.cliente SET saldo = '" + saldo + "' WHERE conta = " + c + ";";
						stmt.executeUpdate(sqlUpdate2);
						
					}catch(Exception j){
						System.out.println("Erro.");
					}
					operacaoSucesso oS = new operacaoSucesso(display);
					oS.setVisible(true);
				}else {
					operacaoFalha oF = new operacaoFalha(display);
					oF.setVisible(true);
				}
			}
		});
		btnConfirmar.setBounds(134, 206, 75, 25);
		btnConfirmar.setText("Confirmar");
		
		Label lblSaldoDisponvel = new Label(this, SWT.NONE);
		lblSaldoDisponvel.setBounds(10, 174, 115, 15);
		lblSaldoDisponvel.setText("Saldo Dispon\u00EDvel\tR$");

		createContents();
	}

	/**
	 * Create contents of the shell.
	 */
	protected void createContents() {
		setText("Depósito");
		setSize(316, 280);

	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
