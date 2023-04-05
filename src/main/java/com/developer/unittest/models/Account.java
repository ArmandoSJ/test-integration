package com.developer.unittest.models;

import com.developer.unittest.exceptions.InsufficientMoneyException;
import lombok.*;

import java.math.BigDecimal;

@Data
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
public class Account {
    @EqualsAndHashCode.Include
    @NonNull
    private User user;
    @EqualsAndHashCode.Include
    @NonNull
    private BigDecimal balance;
    private Bank bank;

    public void debit(BigDecimal amount){
        BigDecimal newBalance = this.balance.subtract(amount);
        if (newBalance.compareTo(BigDecimal.ZERO) < 0){
            throw new InsufficientMoneyException("Insufficient Money");
        }
        this.balance = newBalance;
    }

    public void credit(BigDecimal amount){
        this.balance = this.balance.add(amount);
    }
}
