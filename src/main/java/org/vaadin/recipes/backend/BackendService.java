package org.vaadin.recipes.backend;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class BackendService {

    private List<Employee> employees;

    {
    // Init dummy data

        Long id = 42L;

        employees = new ArrayList<>();
        employees.add(new Employee(id++, "Rowena", "Leeming", "rleeming0@bbc.co.uk", "Food Chemist"));
        employees.add(new Employee(id++, "Alvinia", "Delong", "adelong1@altervista.org", "Recruiting Manager"));
        employees.add(new Employee(id++, "Leodora", "Burry", "lburry2@example.com", "Food Chemist"));
        employees.add(new Employee(id++, "Karen", "Oaten", "koaten3@ihg.com", "VP Sales"));
        employees.add(new Employee(id++, "Mariele", "Huke", "mhuke4@washingtonpost.com", "Research Assistant IV"));
        employees.add(new Employee(id++, "Grata", "Widdowes", "gwiddowes5@cargocollective.com", "Actuary"));
        employees.add(new Employee(id++, "Donna", "Roadknight", "droadknight6@apache.org", "Mechanical Systems Engineer"));
        employees.add(new Employee(id++, "Tommi", "Nowland", "tnowland7@biblegateway.com", "Senior Developer"));
        employees.add(new Employee(id++, "Tonya", "Teresia", "tteresia8@boston.com", "Assistant Manager"));
        employees.add(new Employee(id++, "Steffen", "Yon", "syon9@ocn.ne.jp", "Senior Sales Associate"));
        employees.add(new Employee(id++, "Consalve", "Willes", "cwillesa@linkedin.com", "Programmer I"));
        employees.add(new Employee(id++, "Jeanelle", "Lambertz", "jlambertzb@nymag.com", "Operator"));
        employees.add(new Employee(id++, "Odelia", "Loker", "olokerc@gov.uk", "Developer I"));
        employees.add(new Employee(id++, "Briano", "Shawell", "bshawelld@posterous.com", "Research Assistant IV"));
        employees.add(new Employee(id++, "Tarrance", "Mainston", "tmainstone@cmu.edu", "Research Nurse"));
        employees.add(new Employee(id++, "Torrence", "Gehring", "tgehringf@a8.net", "Geological Engineer"));
        employees.add(new Employee(id++, "Augie", "Pionter", "apionterg@ehow.com", "Senior Financial Analyst"));
        employees.add(new Employee(id++, "Marillin", "Aveson", "mavesonh@shop-pro.jp", "Technical Writer"));
        employees.add(new Employee(id++, "Jacquelyn", "Moreby", "jmorebyi@slashdot.org", "Executive Secretary"));
        employees.add(new Employee(id++, "Glenn", "Bangley", "gbangleyj@prlog.org", "Account Executive"));
        employees.add(new Employee(id++, "Isidoro", "Glave", "iglavek@tamu.edu", "Compensation Analyst"));
        employees.add(new Employee(id++, "Cchaddie", "Spatarul", "cspatarull@sun.com", "Business Systems Development Analyst"));
    }

    public List<Employee> getEmployees() {
        return employees;
    }

}
