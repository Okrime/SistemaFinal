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

public class janelaClienteTransacao extends Shell {
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
			janelaClienteTransacao shell = new janelaClienteTransacao(display);
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
	public janelaClienteTransacao(Display display) {
		super(display, SWT.SHELL_TRIM);
		
		Label label = new Label(this, SWT.NONE);
		label.setText("Conta");
		label.setBounds(49, 13, 41, 15);
		
		Label label_1 = new Label(this, SWT.NONE);
		label_1.setText("Agencia");
		label_1.setBounds(49, 52, 43, 15);
		
		Label label_2 = new Label(this, SWT.NONE);
		label_2.setText("Valor");
		label_2.setBounds(49, 96, 41, 15);
		
		text = new Text(this, SWT.BORDER);
		text.setBounds(103, 10, 138, 21);
		
		text_1 = new Text(this, SWT.BORDER);
		text_1.setBounds(103, 49, 140, 21);
		
		text_2 = new Text(this, SWT.BORDER);
		text_2.setBounds(101, 93, 140, 21);
		
		
		Button button_2 = new Button(this, SWT.NONE);
		button_2.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				dispose();
			}
		});
		button_2.setText("Cancelar");
		button_2.setBounds(211, 210, 75, 25);
		
		Button button_3 = new Button(this, SWT.NONE);
		button_3.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				boolean valid = false;
				int a, c;
				String agencia = text_1.getText();
				String conta = text.getText();
				a = Integer.parseInt(agencia);
				c = Integer.parseInt(conta);
				try{
					Connection Conn = connect.getConnection();
					Statement stmt = (Statement) Conn.createStatement();					
					String sqlBusca2 = "SELECT * FROM new_schema.cliente WHERE conta = " + c + ";";
					ResultSet rs2 = stmt.executeQuery(sqlBusca2);
					rs2.next();
					
					menu.idAlvo = rs2.getInt(1);
					
				}catch(Exception j){
					System.out.println("Erro.");
				}
				
				
				float valor;
				
				String aux;
				aux = text_2.getText();
				valor = Float.parseFloat(aux);
				//agencia = getAgencia(id);
				
				valid = menu.checarAgencia(a);	//checar agencia
				if (valid){
					valid = menu.checarConta(c);	//checar conta
					if (valid){
						valid = menu.checarValor(valor);	//validar saldo
					}
						
				}
				setVisible(false);
				if (valid){
					try{
						Connection Conn2 = connect.getConnection();
						float saldo=0;
						Statement stmt2 = (Statement) Conn2.createStatement();
						menu.saldo = menu.saldo - valor;	//* BD * atualizar saldo no BD
						String sqlUpdate = "UPDATE new_schema.cliente SET saldo = '" + menu.saldo + "' WHERE	clienteid = " + menu.idLogado + ";";
						stmt2.executeUpdate(sqlUpdate);
						String sqlBusca = "SELECT * FROM new_schema.cliente WHERE conta = " + c + ";";
						ResultSet rs = stmt2.executeQuery(sqlBusca);
						rs.next();

						saldo = rs.getFloat(5);
						saldo = saldo + valor;
						String sqlUpdate2 = "UPDATE new_schema.cliente SET saldo = '" + saldo + "' WHERE clienteid = " + menu.idAlvo + ";";
						stmt2.executeUpdate(sqlUpdate2);
						
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
		button_3.setText("Confirmar");
		button_3.setBounds(130, 210, 75, 25);
		
		Label lblSaldoDisponivel = new Label(this, SWT.NONE);
		lblSaldoDisponivel.setBounds(10, 177, 114, 15);
		lblSaldoDisponivel.setText("Saldo Disponivel\tR$");
		
		Label label_value = new Label(this, SWT.NONE);
		label_value.setBounds(130, 177, 41, 15);
		String aux;
		aux = String.valueOf(menu.saldo);
		label_value.setText(aux);	//*BD*
		createContents();
	}

	/**
	 * Create contents of the shell.
	 */
	protected void createContents() {
		setText("Transação");
		setSize(310, 278);

	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

}
