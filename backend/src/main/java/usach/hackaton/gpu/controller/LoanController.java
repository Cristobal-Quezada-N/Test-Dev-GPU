package usach.hackaton.gpu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import usach.hackaton.gpu.entities.Loan;
import usach.hackaton.gpu.service.LoanService;

import java.util.List;

@RestController
@RequestMapping("/api/loans")
public class LoanController {

    @Autowired
    private LoanService loanService;

    // Crear préstamo
    @PostMapping("/createLoan")
    public ResponseEntity<Loan> createLoan(@RequestBody Loan loan) {
        Loan savedLoan = loanService.saveLoan(loan);
        return ResponseEntity.ok(savedLoan);
    }

    // Listar todos los préstamos
    @GetMapping("/getLoans")
    public ResponseEntity<List<Loan>> getAllLoans() {
        return ResponseEntity.ok(loanService.getAllLoans());
    }

    // Buscar préstamo por ID
    @GetMapping("/getById/{id}")
    public ResponseEntity<Loan> getLoanById(@PathVariable Long id) {
        return loanService.getLoanById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Eliminar préstamo por ID
    @DeleteMapping("/deleteById/{id}")
    public ResponseEntity<Void> deleteLoanById(@PathVariable Long id) {
        if (loanService.getLoanById(id).isPresent()) {
            loanService.deleteLoanById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
