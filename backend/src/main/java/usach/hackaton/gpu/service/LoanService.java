package usach.hackaton.gpu.service;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import usach.hackaton.gpu.entities.Item;
import usach.hackaton.gpu.entities.ItemCopy;
import usach.hackaton.gpu.entities.Loan;
import usach.hackaton.gpu.repositories.ItemRepository;
import usach.hackaton.gpu.repositories.LoanRepository;

@Service
public class LoanService {

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private ItemCopyService itemCopyService;

    // Crear o actualizar un préstamo
    public Loan saveLoan(Loan loan) {
        // Buscar el ítem relacionado
        Item item = itemRepository.findById(loan.getItemId())
            .orElseThrow(() -> new IllegalArgumentException("Ítem no encontrado con ID: " + loan.getItemId()));

        ItemCopy itemCopy = itemCopyService.getNextAvailableCopy(item.getId());

        // Marcar como prestado
        itemCopy.setStatus("LOANED");

        itemCopyService.save(itemCopy);

        // Forzar estado inicial del préstamo
        loan.setStatusId(1L);

        // Guardar el préstamo
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

    // Cambiar de "Recibido" (1) a "Aceptado" (2)
    public Loan acceptLoan(Long id) {
        Loan loan = loanRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Préstamo no encontrado con ID: " + id));

        if (loan.getStatusId() != 1) {
            throw new IllegalStateException("El préstamo no está en estado 'Recibido'");
        }

        loan.setStatusId(2L); // Aceptado
        return loanRepository.save(loan);
    }

    // Cambiar de "Recibido" (1) a "Rechazado" (3)
    public Loan rejectLoan(Long id) {
        Loan loan = loanRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Préstamo no encontrado con ID: " + id));

        if (loan.getStatusId() != 1) {
            throw new IllegalStateException("El préstamo no está en estado 'Recibido'");
        }

        loan.setStatusId(3L); // Rechazado
        return loanRepository.save(loan);
    }
}
