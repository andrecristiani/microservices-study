Feature: Create an Order

  Scenario: Salva um pedido valido

    Given Dado um pedido de compra
    And Com os seguintes items
    When Quando tenta salvar o pedido
    Then O pedido eh salvo