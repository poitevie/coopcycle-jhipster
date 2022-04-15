import { entityItemSelector } from '../../support/commands';
import {
  entityTableSelector,
  entityDetailsButtonSelector,
  entityDetailsBackButtonSelector,
  entityCreateButtonSelector,
  entityCreateSaveButtonSelector,
  entityCreateCancelButtonSelector,
  entityEditButtonSelector,
  entityDeleteButtonSelector,
  entityConfirmDeleteButtonSelector,
} from '../../support/entity';

describe('Command e2e test', () => {
  const commandPageUrl = '/command';
  const commandPageUrlPattern = new RegExp('/command(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const commandSample = { addressC: 'Technician PNG', dateC: 45102 };

  let command: any;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/commands+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/commands').as('postEntityRequest');
    cy.intercept('DELETE', '/api/commands/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (command) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/commands/${command.id}`,
      }).then(() => {
        command = undefined;
      });
    }
  });

  it('Commands menu should load Commands page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('command');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Command').should('exist');
    cy.url().should('match', commandPageUrlPattern);
  });

  describe('Command page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(commandPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Command page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/command/new$'));
        cy.getEntityCreateUpdateHeading('Command');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', commandPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/commands',
          body: commandSample,
        }).then(({ body }) => {
          command = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/commands+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [command],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(commandPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Command page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('command');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', commandPageUrlPattern);
      });

      it('edit button click should load edit Command page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Command');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', commandPageUrlPattern);
      });

      it('last delete button click should delete instance of Command', () => {
        cy.intercept('GET', '/api/commands/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('command').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', commandPageUrlPattern);

        command = undefined;
      });
    });
  });

  describe('new Command page', () => {
    beforeEach(() => {
      cy.visit(`${commandPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Command');
    });

    it('should create an instance of Command', () => {
      cy.get(`[data-cy="addressC"]`).type('Research USB magenta').should('have.value', 'Research USB magenta');

      cy.get(`[data-cy="dateC"]`).type('15329').should('have.value', '15329');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        command = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', commandPageUrlPattern);
    });
  });
});
