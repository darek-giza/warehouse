package pl.com.dariusz.giza.financeTracker.service.budget;

import org.springframework.stereotype.Service;
import pl.com.dariusz.giza.financeTracker.domain.budgets.Budget;
import pl.com.dariusz.giza.financeTracker.domain.budgets.Expense;
import pl.com.dariusz.giza.financeTracker.domain.budgets.Income;
import pl.com.dariusz.giza.financeTracker.repositories.BudgetsRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class BudgetServiceImpl implements BudgetService {

    private final BudgetsRepository budgetsRepository;

    public BudgetServiceImpl(BudgetsRepository budgetsRepository) {
        this.budgetsRepository = budgetsRepository;
    }

    @Override
    public Budget findById(Long id) {
        return budgetsRepository.findById(id).get();
    }

    @Override
    public Budget createBudgets() {
        Budget budget = new Budget();
        budget.setBalance(new BigDecimal(0));
        budget.setName("DeafultName");
        return budgetsRepository.save(budget);

    }

    @Override
    public List<Budget> findAll() {
        return budgetsRepository.findAll();
    }


    @Override
    public Budget increaseBudget(Budget budget, List<Income> income) {
        final Optional<Budget> currentBudget = currentBudget(budget);
        BigDecimal balance = currentBudget.get().getBalance();
        checkBalance(balance);
        BigDecimal increase = balance.add(income
                .stream()
                .map(i -> i.getAmount())
                .reduce(BigDecimal::add)
                .get());
        currentBudget.get().setBalance(increase);
        return budgetsRepository.save(currentBudget.get());

    }

    @Override
    public Budget reduceBudget(Budget budget, List<Expense> expense) {
        final Optional<Budget> currentBudget = currentBudget(budget);
        BigDecimal balance = currentBudget(budget).get().getBalance();
        checkBalance(balance);
        BigDecimal reduce = balance.subtract(expense
                .stream().map(e -> e.getAmount())
                .reduce(BigDecimal::add)
                .get());
        currentBudget.get().setBalance(reduce);
        return budgetsRepository.save(currentBudget.get());
    }

    public Optional<Budget> currentBudget(Budget budget) {
        return budgetsRepository.findById(budget.getId());

    }

    public BigDecimal checkBalance(BigDecimal balance) {
        if (balance == null) {
            balance = new BigDecimal(0);
        }
        return balance;
    }


}
