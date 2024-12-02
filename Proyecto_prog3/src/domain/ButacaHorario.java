package domain;

public class ButacaHorario {
	private static int cont = 1;
    private int id;          
    private int idButaca;    
    private String horario;  

    public ButacaHorario(int idButaca, String horario) {
    	this.id = cont;
    	cont++;
    	this.idButaca = idButaca;
        this.horario = horario;
    }

    public ButacaHorario(int id, int idButaca, String horario, int estado) {
        this.id = id;
        this.idButaca = idButaca;
        this.horario = horario;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdButaca() {
        return idButaca;
    }

    public void setIdButaca(int idButaca) {
        this.idButaca = idButaca;
    }

    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }



    @Override
    public String toString() {
        return "ButacaHorario{" +
                "id=" + id +
                ", idButaca=" + idButaca +
                ", horario='" + horario +
                '}';
    }
}
