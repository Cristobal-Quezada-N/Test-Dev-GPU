package usach.hackaton.gpu.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import usach.hackaton.gpu.entities.Loan;
import usach.hackaton.gpu.repositories.LoanRepository;

import java.util.List;
import java.util.Optional;

@Service
public class LoanService {

    @Autowired
    private LoanRepository loanRepository;

    // Crear o actualizar un préstamo
    public Loan saveLoan(Loan loan) {
        return loanRepository.save(loan);
    }

    // Listar todos los préstamos
    public List<Loan> getAllLoans() {
        return loanRepository.findAll();
    }

    // Buscar un préstamo por ID
    public Optional<Loan> getLoanById(Long id) {
        return loanRepository.findById(id);
    }

    // Eliminar un préstamo por objeto
    public void deleteLoan(Loan loan) {
        loanRepository.delete(loan);
    }

    // Eliminar un préstamo por ID
    public void deleteLoanById(Long id) {
        loanRepository.deleteById(id);
    }
}
