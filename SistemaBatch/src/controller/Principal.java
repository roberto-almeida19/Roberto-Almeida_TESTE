package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import model.Account;
import util.ConexaoUtil;

public class Principal {
	
	
	public static void gerarMassas(){
		Random r = new Random();
		for (int i = 0; i < 3000; i++) {
			Account a = new Account();
			a.setCpf_cnpj(Long.valueOf(i));
			a.setIs_active(r.nextBoolean());
			a.setNm_customer("Sthefani "+i+" :P");
			a.setVl_total(r.nextDouble()*1000.2f);
			contas.add(a);
		}
		
		
	}
	public static void fazerBatch(){
		Connection conn = null;
		try{
			conn = ConexaoUtil.getConexao();
			conn.setAutoCommit(false);
			StringBuilder sql = new StringBuilder();
			sql.append("Insert into tb_customer_account (cpf_cnpj,nm_customer, is_active, vl_total) values (?,?,?,?)");
			PreparedStatement pstm = conn.prepareStatement(sql.toString());
			int contador = 0;
			for (Account account : contas) {
				contador++;
				pstm.setLong(1, account.getCpf_cnpj());
				pstm.setString(2, account.getNm_customer());
				pstm.setBoolean(3, account.isIs_active());
				pstm.setDouble(4, account.getVl_total());
				pstm.addBatch();
			}
			if ((contador%100)==0){
				
				pstm.executeBatch();
			}
			conn.commit();
			
		}catch(Exception e ){
			e.printStackTrace();
			try {
				conn.rollback();
			} catch (Exception e2) {
				e.printStackTrace();
			}
		}
	}
	public static void lerBatch(){
		List<Account> contasLidas = new ArrayList<>();
		Connection conn = null;
		try{
			conn = ConexaoUtil.getConexao();
			conn.setAutoCommit(false);
			StringBuilder sql = new StringBuilder();
			sql.append("Select id_customer, cpf_cnpj,nm_customer, is_active, vl_total from tb_customer_account");
			PreparedStatement pstm = conn.prepareStatement(sql.toString());
			ResultSet rs = pstm.executeQuery();
			int contador = 0;

			while(rs.next()){
			contador++;
			Account account = new Account();
				account.setId_customer(rs.getInt("id_customer"));
				account.setCpf_cnpj(rs.getInt("cpf_cnpj"));
				account.setNm_customer(rs.getString("nm_customer"));
				account.setIs_active(rs.getBoolean("is_active"));
				account.setVl_total(rs.getDouble("vl_total"));
				contas.add(account);
				calcularMedia(account);
				
			}
			if ((contador%100)==0){
				
				pstm.executeBatch();
			}
			conn.commit();
			
		}catch(Exception e ){
			e.printStackTrace();
			try {
				conn.rollback();
			} catch (Exception e2) {
				e.printStackTrace();
			}
		}
	}
	
	
	

	public static void calcularMedia(Account c){

		if ((c.getVl_total()>560) && (c.getId_customer()>=1500)&&(c.getId_customer()<=2700)) {
			naMedia.add(c);
		
		}
	}
	public static void main(String[] args) {
		gerarMassas();
		fazerBatch();
		lerBatch();
		System.out.println("Média final: "+imprimirMedia());
	}
	private static double imprimirMedia() {
		double total= 0.0f;
		for (Account account : naMedia) {
			total = (double)account.getVl_total()+total;
		}
		return total;
	}
	static List<Account> naMedia = new ArrayList<>();
	static List<Account> contas = new ArrayList<>();
}
