package in.co.icdswb.hrms.SetGetActivity;

public class User {
    private String emp_desig;
    private String emp_img;
    private String office_close_time;
    private String emp_branch;
    private String emp_name;
    private String emp_id;

    public User(String emp_desig, String emp_img, String office_close_time, String emp_branch, String emp_name, String emp_id){
        this.emp_desig = emp_desig;
        this.emp_img = emp_img;
        this.office_close_time = office_close_time;
        this.emp_branch = emp_branch;
        this.emp_name = emp_name;
        this.emp_id = emp_id;
        }

    public String getEmp_desig() {
        return emp_desig;
    }

    public String getEmp_img() {
        return emp_img;
    }

    public String getOffice_close_time() {
        return office_close_time;
    }

    public String getEmp_branch() {
        return emp_branch;
    }

    public String getEmp_name() {
        return emp_name;
    }

    public String getEmp_id() {
        return emp_id;
    }
}
