package view;
import java.sql.Connection;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Text;

import com.mysql.jdbc.Statement;

import conexao.connect;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import view.menu;

public class janelaClienteSacar extends Shell {
	private Text text;

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String args[]) {
		try {
			Display display = Display.getDefault();
			janelaClienteSacar shell = new janelaClienteSacar(display);
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
	public janelaClienteSacar(Display display) {
		super(display, SWT.SHELL_TRIM);
		
		Label lblSaldoDisponivel = new Label(this, SWT.NONE);
		lblSaldoDisponivel.setBounds(10, 10, 114, 15);
		lblSaldoDisponivel.setText("Saldo Disponivel\tR$");
		
		Label label_value = new Label(this, SWT.NONE);
		label_value.setBounds(124, 10, 41, 15);
		String aux;
		aux = String.valueOf(menu.saldo);
		label_value.setText(aux);
		
		Label lblValorSaque = new Label(this, SWT.NONE);
		lblValorSaque.setBounds(10, 38, 80, 15);
		lblValorSaque.setText("Valor Saque: R$");
		
		Button btnCancelar = new Button(this, SWT.NONE);
		btnCancelar.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				dispose();
			}
		});
		btnCancelar.setBounds(124, 74, 75, 25);
		btnCancelar.setText("Cancelar");
		
		text = new Text(this, SWT.BORDER);
		text.setBounds(96, 35, 103, 21);
		
		Button btnConfirmar = new Button(this, SWT.NONE);
		btnConfirmar.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				boolean valid = false;
				float valor;
				String aux;
				aux = text.getText();
				valor = Float.parseFloat(aux);
				valid = menu.checarValor(valor);	//validar saldo
				setVisible(false);
				if (valid){
					try{
						Connection Conn = connect.getConnection();
						Statement stmt = (Statement) Conn.createStatement();
						menu.saldo = menu.saldo - valor;
						String sqlUpdate = "UPDATE new_schema.cliente SET saldo = '" + menu.saldo + "' WHERE	clienteid = " + menu.idLogado + ";";
						stmt.executeUpdate(sqlUpdate);
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
		btnConfirmar.setBounds(23, 74, 75, 25);
		btnConfirmar.setText("Confirmar");
	
		createContents();
	}

	/**
	 * Create contents of the shell.
	 */
	protected void createContents() {
		setText("Saque");
		setSize(228, 145);

	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

}
