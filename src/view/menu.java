package view;

import java.sql.Connection;
import java.sql.ResultSet;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Label;
import org.eclipse.wb.swt.SWTResourceManager;

import com.mysql.jdbc.Statement;

import conexao.connect;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import view.*;
import org.eclipse.swt.widgets.Button;

public class menu extends Shell {
	public static int loggedType;		//declarar variavel global para usar nas outras classes
											//indica tipo de login, 0 neutro 1 cliente 2 funcionario e 3 gerente
	public static int idLogado;
	public static int idAlvo;
	public static float saldo;
	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String args[]) {
		try {
			Display display = Display.getDefault();
			menu shell = new menu(display);
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
	public menu(Display display) {
		super(display, SWT.SHELL_TRIM);
		
		Menu menu = new Menu(this, SWT.BAR);
		setMenuBar(menu);
		String aux = null;
		try{
			Connection Conn = connect.getConnection();
			Statement stmt = (Statement) Conn.createStatement();
			String sqlBusca = "SELECT * FROM new_schema.login WHERE loginid = " + idLogado + ";";
			ResultSet rs = stmt.executeQuery(sqlBusca);
			rs.next();
			
			if (loggedType == 1){
				Statement stmt2 = (Statement) Conn.createStatement();
				String sqlBusca2 = "SELECT * FROM new_schema.cliente WHERE clienteid = " + idLogado + ";";
				ResultSet rs2 = stmt2.executeQuery(sqlBusca2);
				rs2.next();
				
				saldo = rs2.getFloat(5);
			}
		}catch(Exception j){
			System.out.println("Erro.");
		}
		
		/*/**** LOGIN ****
		MenuItem mntmLogin = new MenuItem(menu, SWT.CASCADE);
		mntmLogin.setText("Login");
		if (loggedType != 0){
			mntmLogin.setEnabled(false);
		}
		
		Menu menu_1 = new Menu(mntmLogin);
		mntmLogin.setMenu(menu_1);
		
		MenuItem mntmCliente_5 = new MenuItem(menu_1, SWT.NONE);
		mntmCliente_5.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				loginCliente lgC = new loginCliente(display);
				lgC.setVisible(true);
			}
		});
		mntmCliente_5.setText("Cliente");
		
		MenuItem mntmFuncionario_4 = new MenuItem(menu_1, SWT.NONE);
		mntmFuncionario_4.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				loginFuncionario lgF = new loginFuncionario(display);
				lgF.setVisible(true);
			}
		});
		mntmFuncionario_4.setText("Funcionario");
		//se cliente for selecionado, loggedtype = 1; se funcionario for selecionado, loggedtype = 2; se gerente, loggedtype = 3
		*/
		
		//**** CADASTRO ****
		MenuItem mntmCadastro = new MenuItem(menu, SWT.CASCADE);
		mntmCadastro.setText("Cadastro");
		if (loggedType < 2)						//cadastro vai estar habilitado apenas quando funcionario ou gerente estiver logado
			mntmCadastro.setEnabled(false);
		
		Menu menu_2 = new Menu(mntmCadastro);
		mntmCadastro.setMenu(menu_2);
		
		MenuItem mntmCliente_1 = new MenuItem(menu_2, SWT.NONE);
		mntmCliente_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				janelaEscolherTipoCliente etc = new janelaEscolherTipoCliente(display);
				etc.setVisible(true);
			}
		});
		mntmCliente_1.setText("Cliente");
		
		MenuItem mntmFuncionario_1 = new MenuItem(menu_2, SWT.NONE);
		if (loggedType != 3)						//cadastro de funcionario vai estar habilitado apenas quando gerente estiver logado
			mntmFuncionario_1.setEnabled(false);
		mntmFuncionario_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				janelaGerenteCadFuncionario gcf = new janelaGerenteCadFuncionario(display);
				gcf.setVisible(true);
			}
		});
		mntmFuncionario_1.setText("Funcionario");
		
		//**** ALTERAR ****
		MenuItem mntmAlterar = new MenuItem(menu, SWT.CASCADE);
		mntmAlterar.setText("Alterar");
		if (loggedType < 2)				//se cadastro nao for 2 nem 3
			mntmAlterar.setEnabled(false);
		
		Menu menu_3 = new Menu(mntmAlterar);
		mntmAlterar.setMenu(menu_3);
		
		MenuItem mntmUsurio_1 = new MenuItem(menu_3, SWT.NONE);
		mntmUsurio_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				janelaAlterarLogin jal = new janelaAlterarLogin(display);
				jal.setVisible(true);
			}
		});
		mntmUsurio_1.setText("Usu\u00E1rio");
		
		//**** EXCLUIR ****
		MenuItem mntmExcluir = new MenuItem(menu, SWT.CASCADE);
		mntmExcluir.setText("Excluir");
		if (loggedType != 3)				//se nao for gerente
			mntmExcluir.setEnabled(false);	//desabilitado
		
		Menu menu_4 = new Menu(mntmExcluir);
		mntmExcluir.setMenu(menu_4);
		
		MenuItem mntmUsurio = new MenuItem(menu_4, SWT.NONE);
		mntmUsurio.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				janelaGerenteExcluir jge = new janelaGerenteExcluir(display);
				jge.setVisible(true);
			}
		});
		mntmUsurio.setText("Usu\u00E1rio");
		
		//**** CLIENTE ****
		MenuItem mntmCliente_4 = new MenuItem(menu, SWT.CASCADE);
		mntmCliente_4.setText("Cliente");
		if (loggedType != 1)
			mntmCliente_4.setEnabled(false);
		
		Menu menu_5 = new Menu(mntmCliente_4);
		mntmCliente_4.setMenu(menu_5);
		
		MenuItem mntmDepositar = new MenuItem(menu_5, SWT.NONE);
		mntmDepositar.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				janelaClienteDepositar jcd = new janelaClienteDepositar(display);
				jcd.setVisible(true);
			}
		});
		mntmDepositar.setText("Depositar");
		
		MenuItem mntmSacar = new MenuItem(menu_5, SWT.NONE);
		mntmSacar.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				janelaClienteSacar jcs = new janelaClienteSacar(display);
				jcs.setVisible(true);
			}
		});
		mntmSacar.setText("Sacar");
		
		MenuItem mntmTransacao = new MenuItem(menu_5, SWT.NONE);
		mntmTransacao.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				janelaClienteTransacao jct = new janelaClienteTransacao(display);
				jct.setVisible(true);
			}
		});
		mntmTransacao.setText("Transacao");
		
		MenuItem mntmPagarBoleto = new MenuItem(menu_5, SWT.NONE);
		mntmPagarBoleto.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				janelaClienteBoleto jcb = new janelaClienteBoleto(display);
				jcb.setVisible(true);
			}
		});
		mntmPagarBoleto.setText("Pagar Boleto");
		
		Label lblBemVindoAo = new Label(this, SWT.NONE);
		lblBemVindoAo.setFont(SWTResourceManager.getFont("Segoe UI", 32, SWT.BOLD));
		lblBemVindoAo.setBounds(101, 29, 252, 177);
		lblBemVindoAo.setText("Bem Vindo\r\n       ao\r\nBanco Doze!");
		
		Button btnSair = new Button(this, SWT.NONE);
		btnSair.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				dispose();
			}
		});
		btnSair.setBounds(349, 212, 75, 25);
		btnSair.setText("Sair");
		
		Label lblLoginStatus = new Label(this, SWT.NONE);
		lblLoginStatus.setBounds(10, 10, 75, 15);
		lblLoginStatus.setText("Login Status:");
		
		Label lblStatuslogin = new Label(this, SWT.NONE);
		lblStatuslogin.setBounds(91, 10, 136, 15);
		changeStatusLoginLabel(lblStatuslogin);
		
		/* mostrar saldo disponivel
		Label lblSaldoDisponvelR = new Label(this, SWT.NONE);
		lblSaldoDisponvelR.setBounds(10, 212, 106, 15);
		lblSaldoDisponvelR.setText("Saldo Dispon\u00EDvel: R$");
		
		Label label = new Label(this, SWT.NONE);
		label.setBounds(122, 212, 55, 15);
		aux = Float.toString(saldo);
		label.setText(aux);
		if (loggedType != 1){
			label.setVisible(false);
			lblSaldoDisponvelR.setVisible(false);
		}
		*/
		createContents();
	}

	/**
	 * Create contents of the shell.
	 */
	protected void createContents() {
		setText("Sistema Banco Doze");
		setSize(450, 300);

	}
	
	public static void changeStatusLoginLabel(Label label) {
		switch(loggedType){
		case 0: label.setText("Deslogado");
			break;
		case 1: label.setText("Cliente Logado");
			break;
		case 2: label.setText("Funcionário Logado");
			break;
		case 3: label.setText("Gerente Logado");
			break;
		}
		
    }
	
	public static boolean checarValor(float valor){
		boolean valid = false;
	
		if (saldo>valor)
			valid = true;
		
		return valid;
	}
	
	public static boolean checarTamanho(int tam, int padrao){
		boolean value = false;
		
		if (tam==padrao)
			value = true;
		
		return value;
	}
	
	public static boolean checarConta(int conta){
		boolean value = false;
		int bdConta=0;
		
		try{
			Connection Conn = connect.getConnection();
			Statement stmt = (Statement) Conn.createStatement();
			String sqlBusca = "SELECT * FROM new_schema.cliente WHERE clienteid = " + idAlvo + ";";
			//Test later
			ResultSet rs = stmt.executeQuery(sqlBusca);
			rs.next();
			
			bdConta = rs.getInt(8);
			
		}catch(Exception j){
			System.out.println("Erro.");
		}
		
		if (bdConta == conta)
			value = true;
		return value;
	}
	
	public static boolean checarAgencia(int agencia){
		boolean value = false;
		int bdAgencia = 0;
		
		try{
			Connection Conn = connect.getConnection();
			Statement stmt = (Statement) Conn.createStatement();
			String sqlBusca = "SELECT * FROM new_schema.cliente WHERE clienteid = " + idAlvo + ";";
			//Test later
			ResultSet rs = stmt.executeQuery(sqlBusca);
			rs.next();
			bdAgencia = rs.getInt(7);
			//System.out.println(bdAgencia);
		}catch(Exception j){
			System.out.println("Erro.");
		}
		
		if (bdAgencia == agencia)
			value = true;
		return value;
	}
	
	public static boolean checarCPF(String CPF){
		boolean value = false;
		String bdCPF = "cpf";	//* BD * puxar CPF do BD
		
		//vai precisar percorrer todos os CPFs no BD pra ver se tem algum que ja existe
		if (bdCPF.equals(CPF))
			value = true;
		return value;
	}
	
	public static boolean checarCNPJ(String CNPJ){
		boolean value = false;
		String bdCNPJ = "cnpj";	//* BD * puxar CNPJ do BD
		
		//vai precisar percorrer todos os CNPJs no BD pra ver se tem algum que ja existe
		if (bdCNPJ.equals(CNPJ))
			value = true;
		return value;
	}
	
	public static boolean checarUser(String user){
		boolean value = false;
		String bdUser;
		
		try{
			Connection Conn = connect.getConnection();
			Statement stmt = (Statement) Conn.createStatement();
			String sqlBusca = "SELECT * FROM new_schema.login WHERE loginnome = '" + user + "';";
			
			//Test later
			ResultSet rs = stmt.executeQuery(sqlBusca);
			rs.next();
			
			idAlvo = rs.getInt(1);
			bdUser = rs.getString(2);
		
			if(bdUser.equals(user)){
				value = true;
			}
			
		}catch(Exception e){
			System.out.println("Erro.");
		}
		
		//vai precisar percorrer todos os users no BD pra ver se tem algum que ja existe
		return value;
	}
	
	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
